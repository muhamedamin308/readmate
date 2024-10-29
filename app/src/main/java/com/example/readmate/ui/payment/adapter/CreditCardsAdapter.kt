package com.example.readmate.ui.payment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.databinding.ItemLayoutPaymentMethodBinding
import com.example.readmate.ui.base.BaseAdapter
import com.example.readmate.util.gone
import com.example.readmate.util.show

/**
 * @author Muhamed Amin Hassan on 12,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class CreditCardsAdapter(
    private val emptyContainerLayout: View
) : BaseAdapter<CreditCard>(DIFF_CALLBACK) {

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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CreditCard>() {
            override fun areItemsTheSame(oldItem: CreditCard, newItem: CreditCard): Boolean =
                oldItem.cardNumber == newItem.cardNumber


            override fun areContentsTheSame(oldItem: CreditCard, newItem: CreditCard): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLayoutPaymentMethodBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(holder: BookViewHolder, item: CreditCard) {
        val binding = holder.binding as ItemLayoutPaymentMethodBinding

        binding.apply {
            tvCardTitle.text = item.cardHolderName
            tvCardNumber.text = "**** **** **** ${item.cardNumber?.substring(12)}"
            tvExpireDate.text = item.expirationDate
        }
    }

}