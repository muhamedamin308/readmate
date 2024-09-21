package com.example.readmate.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.model.firebase.User
import com.example.readmate.data.repo.remote.firebase.FirebaseUserRepository
import com.example.readmate.util.AppState
import com.example.readmate.util.RegisterFieldState
import com.example.readmate.util.RegisterValidation
import com.example.readmate.util.TAG
import com.example.readmate.util.validateEmail
import com.example.readmate.util.validatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

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

    private val _loginState = MutableStateFlow<AppState<User>>(AppState.Ideal())
    val loginState = _loginState.asStateFlow()

    private val _googleAuth = MutableStateFlow<AppState<User>>(AppState.Ideal())
    val googleAuth = _googleAuth.asStateFlow()

    private val _registerValidate = Channel<RegisterFieldState>()
    val registerValidate = _registerValidate.receiveAsFlow()

    fun register(user: User, password: String) {
        val isValidInput = accountValidation(user.email, password)
        if (isValidInput) {
            viewModelScope.launch { _registerState.emit(AppState.Loading()) }
            userRepository.register(user, password) { newAccount, error ->
                Log.d("AuthViewModel$TAG", "register(valid) ${user.email}, $password")
                handleAuthResult(newAccount, error, _registerState)
            }
        } else {
            Log.d("AuthViewModel$TAG", "register(not valid) ${user.email}, $password")
            sendValidationError(user.email, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch { _loginState.emit(AppState.Loading()) }
        userRepository.login(email, password) { user, error ->
            Log.d("AuthViewModel$TAG", "login $email, $password")
            handleAuthResult(user, error, _loginState)
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

    private fun sendValidationError(email: String?, password: String) {
        viewModelScope.launch {
            Log.d("AuthViewModel$TAG", "sendValidationError $email, $password")
            val validationState = RegisterFieldState(
                validateEmail(email!!), validatePassword(password)
            )
            _registerValidate.send(validationState)
        }
    }

    private fun accountValidation(email: String?, password: String): Boolean {
        val emailValidation = validateEmail(email!!)
        val passwordValidate = validatePassword(password)
        return emailValidation is RegisterValidation.Success
                && passwordValidate is RegisterValidation.Success
    }

    private fun handleAuthResult(
        user: User?,
        error: Exception?,
        successState: MutableStateFlow<AppState<User>>
    ) {
        viewModelScope.launch {
            if (error == null) {
                Log.d("AuthViewModel$TAG", "handleAuthResult(success) ${user?.email}")
                successState.value = AppState.Success(user!!)
            } else {
                Log.d("AuthViewModel$TAG", "handleAuthResult(error) ${user?.email}")
                successState.value = AppState.Error(error.message.toString())
            }
        }
    }

}