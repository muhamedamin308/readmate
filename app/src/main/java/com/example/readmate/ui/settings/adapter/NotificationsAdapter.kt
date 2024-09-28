package com.example.readmate.ui.settings.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import com.example.readmate.data.model.local.Notification
import com.example.readmate.databinding.ItemLayoutNotificationsBinding
import com.example.readmate.ui.base.BaseAdapter
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author Muhamed Amin Hassan on 25,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class NotificationsAdapter : BaseAdapter<Notification>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Notification>() {
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean =
                oldItem.timestamp == newItem.timestamp

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutNotificationsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun bind(holder: BookViewHolder, item: Notification) {
        val binding = holder.binding as ItemLayoutNotificationsBinding

        binding.apply {
            tvNotificationContent.text = item.title
            val notificationTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(item.timestamp),
                ZoneId.systemDefault()
            )
            val currentTime = LocalDateTime.now()
            val duration = Duration.between(notificationTime, currentTime)
            tvNotificationDuration.text = when {
                duration.toHours() > 0 -> "${duration.toHours()}h"
                duration.toMinutes() > 0 -> "${duration.toMinutes()}m"
                else -> "Just now"
            }
        }
    }
}