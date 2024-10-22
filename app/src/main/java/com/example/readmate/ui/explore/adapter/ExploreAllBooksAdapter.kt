package com.example.readmate.ui.explore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.responses.BookItemResponse
import com.example.readmate.databinding.ItemLayoutExploreResultBooksBinding
import com.example.readmate.ui.base.BaseAdapter
import com.example.readmate.util.gone
import com.example.readmate.util.show

/**
 * @author Muhamed Amin Hassan on 24,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ExploreAllBooksAdapter(
    private val emptyContainerLayout: View?
) : BaseAdapter<BookItemResponse>(DIFF_CALLBACK) {

    init {
        onListUpdated = { isEmpty, _ ->
            emptyContainerLayout?.let {
                if (isEmpty) {
                    it.show()
                } else {
                    it.gone()
                }
            }
        }
    }

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
        val binding = ItemLayoutExploreResultBooksBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun bind(holder: BookViewHolder, item: BookItemResponse) {
        val binding = holder.binding as ItemLayoutExploreResultBooksBinding

        binding.apply {
            Glide.with(holder.itemView)
                .load(item.image)
                .error(R.drawable.not_found)
                .into(imgBookImage)
            tvBookTitle.text = item.title
            tvBookAuthor.text = item.subtitle.ifEmpty { "No Subtitle" }
        }
    }
}