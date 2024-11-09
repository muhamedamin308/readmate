package com.example.readmate.ui.settings.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import com.example.readmate.data.model.firebase.Notification
import com.example.readmate.databinding.ItemLayoutNotificationsBinding
import com.example.readmate.ui.base.BaseAdapter
import com.example.readmate.util.gone
import com.example.readmate.util.show
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author Muhamed Amin Hassan on 25,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class NotificationsAdapter(
    private val emptyContainerLayout: View
) : BaseAdapter<Notification>(DIFF_CALLBACK) {

    init {
        onListUpdated = { isEmpty, _ ->
            if (isEmpty) {
                emptyContainerLayout.show()
            } else {
                emptyContainerLayout.gone()
            }
        }
    }

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
            tvNotificationContent.text = item.title ?: "No Title"
            tvNotificationBody.text = item.body ?: "No Title"
            val notificationTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(item.timestamp!!),
                ZoneId.systemDefault()
            )
            val currentTime = LocalDateTime.now()
            val duration = Duration.between(notificationTime, currentTime)
            tvNotificationDuration.text = when {
                duration.toHours() > 0 -> {
                    val hours = duration.toHours()
                    if (hours > 100)
                        "more than 3 days"
                    else
                        "${hours}h"
                }

                duration.toMinutes() > 0 -> "${duration.toMinutes()}m"
                else -> "Just now"
            }
        }
    }
}