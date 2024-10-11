package com.example.readmate.data.repo.remote.firebase.user

import com.example.readmate.data.model.firebase.Notification
import com.example.readmate.data.service.remote.firebase.FirebaseUserService

/**
 * @author Muhamed Amin Hassan on 11,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class NotificationsRepositoryImpl(
    private val userService: FirebaseUserService,
) : NotificationsRepository {

    override fun getNotifications(onAction: (List<Notification>?, Exception?) -> Unit) =
        userService.getUserNotifications(onAction)

    override fun saveNotification(notification: Notification) =
        userService.saveUserNotification(notification)

}