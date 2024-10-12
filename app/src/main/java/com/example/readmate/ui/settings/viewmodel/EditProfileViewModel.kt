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
}
