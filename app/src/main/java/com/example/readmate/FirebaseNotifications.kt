package com.example.readmate

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * @author Muhamed Amin Hassan on 06,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseNotifications : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        getFirebaseMessages(
            message.notification?.title,
            message.notification?.body
        )
    }

    @SuppressLint("MissingPermission")
    private fun getFirebaseMessages(title: String?, body: String?) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            "notification"
        ).setSmallIcon(R.drawable.baseline_warning_24)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        val manager = NotificationManagerCompat.from(this)
        manager.notify(102, builder.build())
    }
}