package com.example.readmate.data.repo.remote.firebase.user

import com.example.readmate.data.model.firebase.Notification

/**
 * @author Muhamed Amin Hassan on 11,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
interface NotificationsRepository {
    fun getNotifications(onAction: (List<Notification>?, Exception?) -> Unit)
    fun saveNotification(notification: Notification)
}