package com.example.readmate.ui.settings.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.model.firebase.User
import com.example.readmate.data.repo.remote.firebase.auth.FirebaseUserRepository
import com.example.readmate.util.AppState
import com.example.readmate.util.RegisterValidation
import com.example.readmate.util.validateEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class EditProfileViewModel(
    private val userRepository: FirebaseUserRepository,
    app: Application
) : AndroidViewModel(app) {
    private val _profileState = MutableStateFlow<AppState<User>>(AppState.Ideal())
    val profileState = _profileState.asStateFlow()

    private val _editProfile = MutableStateFlow<AppState<User>>(AppState.Ideal())
    val editProfile = _editProfile.asStateFlow()

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch { _profileState.emit(AppState.Loading()) }
        userRepository.getUserProfile { user, error ->
            error?.let {
                viewModelScope.launch { _profileState.emit(AppState.Error(it.message.toString())) }
            } ?: viewModelScope.launch { _profileState.emit(AppState.Success(user!!)) }
        }
    }

    fun updateUserProfile(user: User, imageUri: Uri?) {
        val nameParts = user.name?.split(" ")
        val firstName = nameParts?.get(0)!!
        val lastName = nameParts[1]
        val validInput = validateEmail(user.email!!) is RegisterValidation.Success
                && firstName.trim().isNotEmpty() && lastName.trim().isNotEmpty()

        if (!validInput) {
            viewModelScope.launch { _editProfile.emit(AppState.Error("Check your input!")) }
            return
        }

        viewModelScope.launch { _editProfile.emit(AppState.Loading()) }
        userRepository.updateUserProfile(user, imageUri) { newUser, error ->
            error?.let {
                viewModelScope.launch { _editProfile.emit(AppState.Error(it.message.toString())) }
            } ?: viewModelScope.launch { _editProfile.emit(AppState.Success(newUser!!)) }
        }
    }
}