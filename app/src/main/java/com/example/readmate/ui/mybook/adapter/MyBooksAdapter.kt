package com.example.readmate.ui.mybook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.firebase.MyBook
import com.example.readmate.databinding.ItemLayoutMyBooksBinding
import com.example.readmate.ui.base.BaseAdapter
import com.example.readmate.util.gone
import com.example.readmate.util.show

/**
 * @author Muhamed Amin Hassan on 24,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class MyBooksAdapter(
    private val emptyContainerLayout: View
) : BaseAdapter<MyBook>(DIFF_CALLBACK) {

    init {
        onListUpdated = { isEmpty, _ ->
            emptyContainerLayout.let {
                if (isEmpty) {
                    it.show()
                } else {
                    it.gone()
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MyBook>() {
            override fun areItemsTheSame(
                oldItem: MyBook,
                newItem: MyBook
            ): Boolean = oldItem.bookId == newItem.bookId

            override fun areContentsTheSame(
                oldItem: MyBook,
                newItem: MyBook
            ): Boolean = oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutMyBooksBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun bind(holder: BookViewHolder, item: MyBook) {
        val binding = holder.binding as ItemLayoutMyBooksBinding

        binding.apply {
            Glide.with(holder.itemView)
                .load(item.image)
                .error(R.drawable.dummy_book)
                .into(imgBookImage)
            tvBookTitle.text = item.title
            tvBookAuthor.text = item.author
        }
    }
}