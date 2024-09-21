package com.example.readmate.ui.onboarding.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.repo.remote.firebase.FirebaseUserRepository
import com.example.readmate.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 10,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class OnBoardingViewModel(
    private val userRepository: FirebaseUserRepository,
    private val preferences: SharedPreferences
) : ViewModel() {
    private val _appState = MutableStateFlow(0)
    val appState = _appState.asStateFlow()

    init {
        val isStarted = preferences.getBoolean(
            Constants.SHARED_PREFERENCES_KEY,
            false
        )
        if (userRepository.isUserLoggedIn)
            viewModelScope.launch { _appState.emit(Constants.HOME_ACTIVITY_ID) }
        else if (isStarted)
            viewModelScope.launch { _appState.emit(Constants.signInPath) }
        else Unit
    }

    fun activateGetStarted() =
        preferences.edit().putBoolean(
            Constants.SHARED_PREFERENCES_KEY,
            true
        ).apply()
}