package com.example.readmate.ui.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.databinding.ItemLayoutHomeRecommendedBooksBinding

/**
 * @author Muhamed Amin Hassan on 23,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class RecommendedBooksAdapter : BaseBookAdapter<Book>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean =
                oldItem.bookId == newItem.bookId

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutHomeRecommendedBooksBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun bind(holder: BookViewHolder, item: Book) {
        val binding = holder.binding as ItemLayoutHomeRecommendedBooksBinding

        binding.apply {
            Glide.with(holder.itemView)
                .load(item.image)
                .error(R.drawable.not_found)
                .into(imgBookImage)
            tvBookTitle.text = item.title
            tvBookAuthor.text = item.author
        }
    }
}