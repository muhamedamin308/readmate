package com.example.readmate.ui.library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Review
import com.example.readmate.databinding.ItemLayoutBookReviewBinding
import com.example.readmate.ui.base.BaseAdapter
import com.example.readmate.util.gone
import com.example.readmate.util.show
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author Muhamed Amin Hassan on 23,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class BookReviewsAdapter(
    private val emptyView: View
) : BaseAdapter<Review>(DIFF_CALLBACK) {

    init {
        onListUpdated = { isEmpty, _ ->
            if (isEmpty) {
                emptyView.show()
            } else {
                emptyView.gone()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean =
                oldItem.timestamp == newItem.timestamp

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutBookReviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun bind(holder: BookViewHolder, item: Review) {
        val binding = holder.binding as ItemLayoutBookReviewBinding

        binding.apply {
            Glide.with(holder.itemView)
                .load(item.user?.profileImage)
                .error(R.drawable.not_found)
                .into(reviewUserImage)
            tvComment.text = item.comment ?: "No comment"
            tvReviewUsername.text = item.user?.name ?: "No username"
            val notificationTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(item.timestamp!!),
                ZoneId.systemDefault()
            )
            val currentTime = LocalDateTime.now()
            val duration = Duration.between(notificationTime, currentTime)
            tvReviewDuration.text = when {
                duration.toHours() > 0 -> "${duration.toHours()}h"
                duration.toMinutes() > 0 -> "${duration.toMinutes()}m"
                else -> "Just now"
            }
        }
    }
}