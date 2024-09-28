package com.example.readmate.ui.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.readmate.R
import com.example.readmate.data.model.local.HelpAndSupport
import com.example.readmate.databinding.ItemLayoutHelpSupportBinding
import com.example.readmate.ui.base.BaseAdapter


/**
 * @author Muhamed Amin Hassan on 28,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class HelpAndSupportAdapter : BaseAdapter<HelpAndSupport>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HelpAndSupport>() {
            override fun areItemsTheSame(
                oldItem: HelpAndSupport,
                newItem: HelpAndSupport
            ): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: HelpAndSupport,
                newItem: HelpAndSupport
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutHelpSupportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun bind(holder: BookViewHolder, item: HelpAndSupport) {
        val binding = holder.binding as ItemLayoutHelpSupportBinding
        binding.apply {
            tvTitle.text = item.title
            tvDescription.text = item.description
            constraintLayout.setOnClickListener {
                isAnyExtended(holder.adapterPosition)
                item.isExtendable = !item.isExtendable
                notifyItemChanged(holder.adapterPosition, Unit)
            }
            if (item.isExtendable) {
                imageShowDetails.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
                tvDescription.visibility = View.VISIBLE
            } else {
                collapseExtendedViews(binding)
            }
        }
    }

    override fun onBindViewHolder(
        holder: BookViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] == 0)
            collapseExtendedViews(holder.binding as ItemLayoutHelpSupportBinding)
        else
            super.onBindViewHolder(holder, position, payloads)
    }

    private fun collapseExtendedViews(binding: ItemLayoutHelpSupportBinding) {
        binding.tvDescription.visibility = View.GONE
        binding.imageShowDetails.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
    }

    private fun isAnyExtended(adapterPosition: Int) {
        val temp = differ.currentList.indexOfFirst {
            it.isExtendable
        }
        if (temp >= 0 && temp != adapterPosition) {
            differ.currentList[temp].isExtendable = false
            notifyItemChanged(temp, 0)
        }
    }

}