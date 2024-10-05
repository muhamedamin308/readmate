package com.example.readmate.ui.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.responses.BookItemResponse
import com.example.readmate.databinding.ItemLayoutHomeRecommendedBooksBinding
import com.example.readmate.ui.base.BaseAdapter

/**
 * @author Muhamed Amin Hassan on 05,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ExploreSimilarBooksAdapter : BaseAdapter<BookItemResponse>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookItemResponse>() {
            override fun areItemsTheSame(
                oldItem: BookItemResponse,
                newItem: BookItemResponse
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: BookItemResponse,
                newItem: BookItemResponse
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutHomeRecommendedBooksBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun bind(holder: BookViewHolder, item: BookItemResponse) {
        val binding = holder.binding as ItemLayoutHomeRecommendedBooksBinding

        binding.apply {
            Glide.with(holder.itemView)
                .load(item.image)
                .error(R.drawable.not_found)
                .into(imgBookImage)
            tvBookAuthor.text = if (item.authors.length >= 24) {
                item.authors.substring(0, item.authors.length - 6) + "...."
            } else {
                item.authors
            }
            tvBookTitle.text = if (item.title.length >= 20) {
                item.title.substring(0, item.title.length - 6) + "...."
            } else {
                item.title
            }
        }
    }


}