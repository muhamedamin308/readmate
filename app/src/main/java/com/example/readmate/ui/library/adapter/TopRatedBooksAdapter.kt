package com.example.readmate.ui.library.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.databinding.ItemLayoutTopRatedBooksBinding
import com.example.readmate.ui.base.BaseAdapter
import com.example.readmate.util.generateRandomColor

/**
 * @author Muhamed Amin Hassan on 23,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class TopRatedBooksAdapter : BaseAdapter<Book>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean =
                oldItem.bookId == newItem.bookId

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutTopRatedBooksBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    @SuppressLint("DefaultLocale")
    override fun bind(holder: BookViewHolder, item: Book) {
        val binding = holder.binding as ItemLayoutTopRatedBooksBinding

        binding.apply {
            Glide.with(holder.itemView)
                .load(item.image)
                .error(R.drawable.dummy_book)
                .into(imgBookImage)

            val truncatedOverview = if (item.overview!!.length > 60) {
                item.overview.substring(0, item.overview.length / 2) + "..."
            } else {
                item.overview
            }
            topRatedItemLayout.setBackgroundColor(
                generateRandomColor(
                    holder.itemView.context,
                    holder.adapterPosition
                )
            )
            tvBookOverview.text = truncatedOverview
            tvBookTitle.text = item.title
            tvBookAvgRating.text = String.format("%.1f", item.averageRating)
        }
    }
}