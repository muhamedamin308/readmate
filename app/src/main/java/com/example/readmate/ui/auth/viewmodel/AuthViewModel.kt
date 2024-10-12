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
import kotlinx.coroutines.flow.MutableStateFlow
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
    val registerState = _registerState.asStateFlow()

    private val _loginState = MutableStateFlow<AppState<FirebaseUser>>(AppState.Ideal())
    val loginState = _loginState.asStateFlow()

    private val _googleAuth = MutableStateFlow<AppState<User>>(AppState.Ideal())
    val googleAuth = _googleAuth.asStateFlow()

    private val _registerValidate = Channel<RegisterFieldState>()
    val registerValidate = _registerValidate.receiveAsFlow()

    fun register(user: User, password: String) {
        if (accountValidation(user.email, password)) {
            updateAppState(_registerState, AppState.Loading())
            userRepository.register(user, password) { newUser, exception ->
                handleResult(_registerState, newUser, exception)
            }
        } else {
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email!!), validatePassword(password)
            )
            runBlocking { _registerValidate.send(registerFieldState) }
        }
    }

    fun login(email: String, password: String) {
        updateAppState(_loginState, AppState.Loading())
        userRepository.login(email, password) { firebaseUser, exception ->
            handleResult(_loginState, firebaseUser, exception)
        }
    }

    fun googleSignIn() = userRepository.signInWithGoogle()

    fun authWithGoogle(token: String) {
        userRepository.authWithGoogle(token) { user, error ->
            viewModelScope.launch {
                handleResult(_googleAuth, user, error)
            }
        }
    }

    private fun <T> handleResult(
        stateFlow: MutableStateFlow<AppState<T>>,
        result: T?,
        exception: Exception?
    ) {
        exception?.let {
            updateAppState(stateFlow, AppState.Error(it.message ?: "An error occurred"))
        } ?: updateAppState(stateFlow, AppState.Success(result!!))
    }

    private fun <T> updateAppState(
        stateFlow: MutableStateFlow<AppState<T>>,
        newState: AppState<T>
    ) {
        viewModelScope.launch { stateFlow.emit(newState) }
    }

    private fun accountValidation(email: String?, password: String): Boolean {
        val emailValidation = validateEmail(email!!)
        val passwordValidate = validatePassword(password)
        return emailValidation is RegisterValidation.Success && passwordValidate is RegisterValidation.Success
    }
}
