package com.example.readmate.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 16,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
abstract class BaseViewModel : ViewModel() {

    protected fun <T> handleResult(
        stateFlow: MutableStateFlow<AppState<T>>,
        result: T?,
        exception: Exception?
    ) {
        exception?.let {
            updateAppState(stateFlow, AppState.Error(it.message ?: "An error occurred"))
        } ?: updateAppState(stateFlow, AppState.Success(result!!))
    }

    protected fun <T> updateAppState(
        stateFlow: MutableStateFlow<AppState<T>>,
        newState: AppState<T>
    ) {
        viewModelScope.launch { stateFlow.emit(newState) }
    }
}