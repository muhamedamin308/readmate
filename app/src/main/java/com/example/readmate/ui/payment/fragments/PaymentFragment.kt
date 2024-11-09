package com.example.readmate.ui.payment.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.data.model.firebase.MyBook
import com.example.readmate.data.model.local.DiscountState
import com.example.readmate.databinding.FragmentPaymentBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.payment.viewmodel.PaymentViewModel
import com.example.readmate.util.AppState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class PaymentFragment : BaseFragment<FragmentPaymentBinding>() {
    private val navArgs by navArgs<PaymentFragmentArgs>()
    private lateinit var book: MyBook
    private lateinit var creditCard: CreditCard
    private val viewModel by viewModel<PaymentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = navArgs.myBook
        creditCard = navArgs.creditCard
    }

    @SuppressLint("SetTextI18n")
    override fun onViewReady() {
        super.onViewReady()
        fillBillDetails()
        navigateBack(binding.navigateBack)

        binding.btnApplyCode.setOnClickListener {
            val enteredCode = binding.etPromoCode.text.toString().trim()
            if (enteredCode.isNotEmpty()) {
                viewModel.applyPromoCode(enteredCode)
                observeDiscountState()
            } else
                showMessage("Please enter code!!")
        }
    }

    private fun observeDiscountState() {
        lifecycleScope.launchWhenStarted {
            viewModel.discountPercentage.collect {
                when (it) {
                    is AppState.Error -> {
                        binding.promoCodeMessage.apply {
                            text = it.message
                            setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.errorColor
                                )
                            )
                        }
                        viewVisibility(binding.paymentProgressBar, false)
                    }

                    is AppState.Ideal -> Unit
                    is AppState.Loading -> viewVisibility(binding.paymentProgressBar, true)
                    is AppState.Success -> {
                        val discountResult = it.data
                        viewVisibility(binding.paymentProgressBar, false)
                        binding.etPromoCode.isEnabled = false
                        binding.btnApplyCode.isEnabled = false
                        applyDiscountToTotal(discountResult!!)
                        binding.promoCodeMessage.text = discountResult.message
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fillBillDetails() {
        binding.apply {
            Glide.with(this@PaymentFragment)
                .load(book.image)
                .error(R.drawable.dummy_book)
                .into(imgBookImage)
            tvBookTitle.text = book.title
            tvBookAuthors.text = book.author
            tvCardTitle.text = creditCard.cardHolderName
            tvCardNumber.text = "**** **** **** ${creditCard.cardNumber?.substring(12)}"
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            tvPaymentDate.text = currentDateTime.format(formatter)
            val bookPrice = book.price!!
            tvBookPrice.text = "$${bookPrice}"
            book.bookState?.let {
                tvPaymentDiscount.text = "$${it.getDiscountValue(bookPrice)}"
                tvPaymentTaxes.text = "$${it.getTaxValue(bookPrice)}"
                tvPaymentTotalPrice.text = "$${it.getTotalPrice(bookPrice)}"
                tvBookState.text = it.name
            }
        }
    }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentPaymentBinding =
        FragmentPaymentBinding.inflate(layoutInflater)


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun applyDiscountToTotal(discountPercentage: DiscountState) {
        val originalPrice = book.price ?: 0f
        val stateDiscount = book.bookState?.getDiscountValue(originalPrice) ?: 0f
        val discountedPrice = originalPrice - stateDiscount

        val additionalDiscount = discountedPrice * discountPercentage.discount
        val finalPriceAfterPromo = discountedPrice - additionalDiscount

        val tax = book.bookState?.getTaxValue(finalPriceAfterPromo) ?: 0f
        val totalPrice = finalPriceAfterPromo + tax

        binding.apply {
            tvPaymentDiscount.text = "$${String.format("%.2f", stateDiscount + additionalDiscount)}"
            tvPaymentTotalPrice.text = "$${String.format("%.2f", totalPrice)}"
            promoCodeMessage.text = discountPercentage.message
            promoCodeMessage.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.successColor
                )
            )
        }
    }

}