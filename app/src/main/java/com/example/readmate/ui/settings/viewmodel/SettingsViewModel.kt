package com.example.readmate.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.model.firebase.User
import com.example.readmate.data.repo.remote.firebase.auth.FirebaseUserRepository
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class SettingsViewModel(
    private val userRepository: FirebaseUserRepository
) : ViewModel() {
    private val _userProfileState = MutableStateFlow<AppState<User>>(AppState.Ideal())
    val userProfileState = _userProfileState.asStateFlow()

    init {
        fetchUserProfile()
    }


    fun fetchUserProfile() {
        viewModelScope.launch { _userProfileState.emit(AppState.Loading()) }
        userRepository.getUserProfile { user, error ->
            error?.let {
                viewModelScope.launch { _userProfileState.emit(AppState.Error(it.message.toString())) }
            } ?: viewModelScope.launch { _userProfileState.emit(AppState.Success(user!!)) }
        }
    }

    fun signOut(onLogoutComplete: (Boolean) -> Unit) {
        userRepository.signOut { isLoggedOut ->
            if (isLoggedOut) {
                onLogoutComplete(true)
            } else {
                onLogoutComplete(false)
            }
        }
    }
}