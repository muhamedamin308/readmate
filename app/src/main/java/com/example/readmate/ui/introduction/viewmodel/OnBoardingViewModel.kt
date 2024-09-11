package com.example.readmate.ui.introduction.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.service.firebase.FirebaseAuthService
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
    private val authService: FirebaseAuthService,
    private val preferences: SharedPreferences
) : ViewModel() {
    private val _appState = MutableStateFlow(0)
    val appState = _appState.asStateFlow()

    init {
        val isStarted = preferences.getBoolean(
            Constants.sharedPreferencesKey,
            false
        )
        if (authService.isLoggedIn)
            viewModelScope.launch { _appState.emit(Constants.homeActivityId) }
        else if (isStarted)
            viewModelScope.launch { _appState.emit(Constants.signInPath) }
        else Unit
    }

    fun activateGetStarted() =
        preferences.edit().putBoolean(
            Constants.sharedPreferencesKey,
            true
        ).apply()
}