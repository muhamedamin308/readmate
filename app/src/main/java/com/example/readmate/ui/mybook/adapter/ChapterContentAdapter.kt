package com.example.readmate.ui.mybook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.readmate.data.model.firebase.HeadLine
import com.example.readmate.databinding.ItemLayoutChapterContentBinding
import com.example.readmate.ui.base.BaseAdapter

/**
 * @author Muhamed Amin Hassan on 09,November,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ChapterContentAdapter : BaseAdapter<HeadLine>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HeadLine>() {
            override fun areItemsTheSame(
                oldItem: HeadLine,
                newItem: HeadLine
            ): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: HeadLine,
                newItem: HeadLine
            ): Boolean = oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutChapterContentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun bind(holder: BookViewHolder, item: HeadLine) {
        val binding = holder.binding as ItemLayoutChapterContentBinding

        binding.apply {
            tvChapterTitle.text = item.title
            tvChapterContent.text = item.content
        }
    }
}