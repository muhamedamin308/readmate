package com.example.readmate.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.model.firebase.User
import com.example.readmate.data.repo.remote.firebase.auth.FirebaseUserRepository
import com.example.readmate.util.AppState
import com.example.readmate.util.RegisterFieldState
import com.example.readmate.util.RegisterValidation
import com.example.readmate.util.validateEmail
import com.example.readmate.util.validatePassword
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class AuthViewModel(
    private val userRepository: FirebaseUserRepository
) : ViewModel() {
    private val _registerState = MutableStateFlow<AppState<User>>(AppState.Ideal())
    val registerState = _registerState.asSharedFlow()

    private val _loginState = MutableSharedFlow<AppState<FirebaseUser>>()
    val loginState = _loginState.asSharedFlow()

    private val _googleAuth = MutableStateFlow<AppState<User>>(AppState.Ideal())
    val googleAuth = _googleAuth.asStateFlow()

    private val _registerValidate = Channel<RegisterFieldState>()
    val registerValidate = _registerValidate.receiveAsFlow()

    fun register(user: User, password: String) {
        val isUserValid = accountValidation(user.email, password)
        if (isUserValid) {
            runBlocking { _registerState.emit(AppState.Loading()) }
            userRepository.register(user, password) { newUser, exception ->
                if (exception == null) {
                    _registerState.value = AppState.Success(newUser!!)
                } else {
                    _registerState.value = AppState.Error(exception.message.toString())
                }
            }
        } else {
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email!!), validatePassword(password)
            )
            runBlocking { _registerValidate.send(registerFieldState) }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch { _loginState.emit(AppState.Loading()) }
        userRepository.login(email, password) { firebaseUser, exception ->
            if (exception == null)
                viewModelScope.launch { _loginState.emit(AppState.Success(firebaseUser!!)) }
            else
                viewModelScope.launch { _loginState.emit(AppState.Error(exception.message.toString())) }
        }
    }

    fun googleSignIn() = userRepository.signInWithGoogle()

    fun authWithGoogle(token: String) {
        userRepository.authWithGoogle(token) { user, error ->
            viewModelScope.launch {
                if (error == null) {
                    _googleAuth.value = AppState.Success(user!!)
                } else {
                    _googleAuth.value = AppState.Error(error.message.toString())
                }
            }
        }
    }

    private fun accountValidation(email: String?, password: String): Boolean {
        val emailValidation = validateEmail(email!!)
        val passwordValidate = validatePassword(password)
        return emailValidation is RegisterValidation.Success
                && passwordValidate is RegisterValidation.Success
    }
}