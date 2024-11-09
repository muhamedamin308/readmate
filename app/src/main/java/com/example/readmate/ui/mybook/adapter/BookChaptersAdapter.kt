package com.example.readmate.ui.mybook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.data.model.firebase.Chapter
import com.example.readmate.databinding.ItemLayoutBookContentBinding
import com.example.readmate.ui.base.BaseAdapter

/**
 * @author Muhamed Amin Hassan on 09,November,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class BookChaptersAdapter : BaseAdapter<Chapter>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Chapter>() {
            override fun areItemsTheSame(
                oldItem: Chapter,
                newItem: Chapter
            ): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: Chapter,
                newItem: Chapter
            ): Boolean = oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutBookContentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun bind(holder: BookViewHolder, item: Chapter) {
        val binding = holder.binding as ItemLayoutBookContentBinding

        binding.apply {
            tvChapterTitle.text = item.title
            val chapterContentAdapter = ChapterContentAdapter()
            chapterContentAdapter.submitList(item.headlines)
            binding.recBookChapterContent.layoutManager =
                LinearLayoutManager(holder.itemView.context)
            binding.recBookChapterContent.adapter = chapterContentAdapter
        }
    }
}