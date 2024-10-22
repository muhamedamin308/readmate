package com.example.readmate.ui.payment.fragments

import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.databinding.FragmentPaymentMethodsBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.payment.adapter.CreditCardsAdapter
import com.example.readmate.ui.payment.viewmodel.PaymentViewModel
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class PaymentMethodsFragment : BaseFragment<FragmentPaymentMethodsBinding>() {
    private val viewModel by viewModel<PaymentViewModel>()
    private val cardsAdapter by lazy { CreditCardsAdapter(binding.containerEmptyList) }
    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentPaymentMethodsBinding =
        FragmentPaymentMethodsBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setupRecyclerView(
            binding.recyclerPaymentMethods,
            cardsAdapter,
            LinearLayoutManager.VERTICAL
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.allCreditCards.collectLatest {
                        when (it) {
                            is AppState.Error -> {
                                viewVisibility(binding.paymentMethodProgressBar, false)
                                showMessage(it.message)
                            }

                            is AppState.Ideal -> Unit
                            is AppState.Loading -> viewVisibility(
                                binding.paymentMethodProgressBar,
                                true
                            )

                            is AppState.Success -> {
                                viewVisibility(binding.paymentMethodProgressBar, false)
                                cardsAdapter.submitList(it.data!!)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchCreditCards()
    }
}