package com.example.readmate

import android.annotation.SuppressLint
import com.example.readmate.data.model.firebase.Notification
import com.example.readmate.ui.settings.viewmodel.NotificationsViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject

/**
 * @author Muhamed Amin Hassan on 06,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseNotifications : FirebaseMessagingService() {

    private val notificationViewModel: NotificationsViewModel by inject()
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: message.data["title"]
        val body = message.notification?.body ?: message.data["body"]
        val timestamp = System.currentTimeMillis()

        if (title != null && body != null) {

            val notification = Notification(
                title = title,
                body = body,
                timestamp = timestamp
            )

            notificationViewModel.saveNotification(notification)

        }
    }
}
