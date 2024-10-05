package com.example.readmate.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author Muhamed Amin Hassan on 23,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

abstract class BaseAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>
) : RecyclerView.Adapter<BaseAdapter<T>.BookViewHolder>() {

    inner class BookViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, diffCallback)

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder

    abstract fun bind(holder: BookViewHolder, item: T)

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = differ.currentList[position]
        bind(holder, item)
        holder.itemView.setOnClickListener { onClick?.invoke(item) }
    }

    override fun getItemCount(): Int = differ.currentList.size

    var onClick: ((T) -> Unit)? = null
}