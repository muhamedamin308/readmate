package com.example.readmate.ui.settings.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.databinding.ItemLayoutBookcaseBinding
import com.example.readmate.ui.base.BaseAdapter
import com.example.readmate.util.gone
import com.example.readmate.util.show

/**
 * @author Muhamed Amin Hassan on 02,November,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class BookcaseAdapter(
    private val emptyContainerLayout: View
) : BaseAdapter<Book>(DIFF_CALLBACK) {

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
        val binding = ItemLayoutBookcaseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(holder: BookViewHolder, item: Book) {
        val binding = holder.binding as ItemLayoutBookcaseBinding

        binding.apply {
            Glide.with(holder.itemView)
                .load(item.image)
                .error(R.drawable.dummy_book)
                .into(imgBookImage)
            tvBookTitle.text = item.title
            tvBookAuthor.text = item.author
            tvBookPrice.text = item.price.toString()
            tvBookRate.text = item.averageRating.toString()
        }
    }
}