package com.example.readmate.ui.payment.fragments

import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.databinding.FragmentAddCreditCardBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.payment.viewmodel.PaymentViewModel
import com.example.readmate.util.AppState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class AddCreditCardFragment : BaseFragment<FragmentAddCreditCardBinding>() {
    private val viewModel by viewModel<PaymentViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentAddCreditCardBinding =
        FragmentAddCreditCardBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        binding.apply {
            btnAddCard.setOnClickListener {
                val cardNumber = etCardNumber.text.toString()
                val cardHolderName = etCardHolderName.text.toString()
                val cardExpiryDate = etCardExpiredDate.text.toString()
                val cardCvv = etCardCvv.text.toString()
                val newCreditCard = CreditCard(
                    cardHolderName = cardHolderName,
                    cardNumber = cardNumber,
                    expirationDate = cardExpiryDate,
                    cvv = cardCvv
                )
                viewModel.addCreditCard(creditCard = newCreditCard)
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.newCreditCard.collect {
                        when (it) {
                            is AppState.Error -> {
                                showMessage(it.message)
                                viewVisibility(binding.signupProgressBar, false)
                            }

                            is AppState.Ideal -> Unit
                            is AppState.Loading -> viewVisibility(binding.signupProgressBar, true)
                            is AppState.Success -> {
                                viewVisibility(binding.signupProgressBar, false)
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }
}