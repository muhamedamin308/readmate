package com.example.readmate.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.model.firebase.Notification
import com.example.readmate.data.repo.remote.firebase.user.NotificationsRepository
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 09,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class NotificationsViewModel(
    private val notificationsRepository: NotificationsRepository,
) : ViewModel() {
    private val _notifications = MutableStateFlow<AppState<List<Notification>>>(AppState.Ideal())
    val notifications = _notifications.asStateFlow()

    init {
        fetchUserNotifications()
    }

    private fun fetchUserNotifications() {
        viewModelScope.launch { _notifications.emit(AppState.Loading()) }
        notificationsRepository.getNotifications { allNotifications, exception ->
            if (exception == null)
                viewModelScope.launch {
                    _notifications.emit(AppState.Success(allNotifications!!))
                }
            else
                viewModelScope.launch {
                    _notifications.emit(AppState.Error(exception.message!!))
                }
        }
    }

    fun saveNotification(notification: Notification) {
        notificationsRepository.saveNotification(notification)
    }
}