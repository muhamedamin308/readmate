package com.example.readmate.ui.library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.databinding.ItemLayoutExploreResultBooksBinding
import com.example.readmate.ui.base.BaseAdapter
import com.example.readmate.util.gone
import com.example.readmate.util.show

/**
 * @author Muhamed Amin Hassan on 24,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ShowAllBooksAdapter(
    private val emptyView: View?
) : BaseAdapter<Book>(DIFF_CALLBACK) {

    init {
        onListUpdated = { isEmpty, _ ->
            emptyView?.let {
                if (isEmpty) {
                    it.show()
                } else {
                    it.gone()
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(
                oldItem: Book,
                newItem: Book
            ): Boolean = oldItem.bookId == newItem.bookId

            override fun areContentsTheSame(
                oldItem: Book,
                newItem: Book
            ): Boolean = oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutExploreResultBooksBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun bind(holder: BookViewHolder, item: Book) {
        val binding = holder.binding as ItemLayoutExploreResultBooksBinding

        binding.apply {
            Glide.with(holder.itemView)
                .load(item.image)
                .error(R.drawable.not_found)
                .into(imgBookImage)
            tvBookTitle.text = item.title
            tvBookAuthor.text = item.subTitle?.ifEmpty { "No Subtitle" }
        }
    }
}