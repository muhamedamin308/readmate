package com.example.readmate.ui.settings.viewmodel

import android.net.Uri
import com.example.readmate.data.model.firebase.User
import com.example.readmate.data.repo.remote.firebase.auth.FirebaseUserRepository
import com.example.readmate.ui.base.BaseViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.RegisterValidation
import com.example.readmate.util.validateEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class EditProfileViewModel(
    private val userRepository: FirebaseUserRepository
) : BaseViewModel() {

    private val _profileState = MutableStateFlow<AppState<User>>(AppState.Ideal())
    val profileState = _profileState.asStateFlow()

    private val _editProfile = MutableStateFlow<AppState<User>>(AppState.Ideal())
    val editProfile = _editProfile.asStateFlow()

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        updateAppState(_profileState, AppState.Loading())
        userRepository.getUserProfile { user, error ->
            handleResult(_profileState, user, error)
        }
    }

    fun updateUserProfile(user: User, imageUri: Uri?) {
        val nameParts = user.name?.split(" ")
        val firstName = nameParts?.get(0) ?: "unknown"
        val lastName = nameParts?.get(1) ?: "unknown"
        val validInput = isValidInput(user.email, firstName, lastName)

        if (validInput) {
            updateAppState(_editProfile, AppState.Loading())
            userRepository.updateUserProfile(user, imageUri) { newUser, error ->
                handleResult(_editProfile, newUser, error)
            }
        } else {
            updateAppState(_editProfile, AppState.Error("Check your input!"))
        }
    }

    private fun isValidInput(email: String?, firstName: String, lastName: String): Boolean {
        return validateEmail(email!!) is RegisterValidation.Success &&
                firstName.trim().isNotEmpty() &&
                lastName.trim().isNotEmpty()
    }
}
