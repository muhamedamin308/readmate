package com.example.readmate.ui.settings.fragments

import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.databinding.FragmentViewPaymentMethodsBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.dialog.addNewCreditCardDialog
import com.example.readmate.ui.dialog.customAlertDialog
import com.example.readmate.ui.payment.adapter.CreditCardsAdapter
import com.example.readmate.ui.payment.viewmodel.PaymentViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.hideBottomNavigation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class FragmentViewPaymentMethods : BaseFragment<FragmentViewPaymentMethodsBinding>() {
    private val viewModel by viewModel<PaymentViewModel>()
    private val cardsAdapter by lazy { CreditCardsAdapter(binding.containerEmptyList) }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentViewPaymentMethodsBinding =
        FragmentViewPaymentMethodsBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setupRecyclerView()
        setupAddPaymentButton()
        setupSwipeToDelete()
        setupObservers()
        navigateBack(binding.navigateBack)

        showMessage("Swap any card to remove!")
    }

    private fun setupRecyclerView() {
        setupRecyclerView(
            binding.recyclerPaymentMethods,
            cardsAdapter,
            LinearLayoutManager.VERTICAL
        )
    }

    private fun setupAddPaymentButton() {
        binding.addPayment.setOnClickListener {
            addNewCreditCardDialog(viewModel) { creditCard ->
                viewModel.addCreditCard(creditCard)
            }
        }
    }


    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val creditCard = cardsAdapter.getItem(position)
                customAlertDialog(
                    title = "Delete Card?",
                    message = "This can't be undone.",
                    positiveTitle = "Delete",
                    onPositiveAction = { viewModel.removeCreditCard(creditCard) },
                    onNegativeAction = { cardsAdapter.notifyItemChanged(position) }
                )
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerPaymentMethods)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allCreditCards.collectLatest { state ->
                    handleCreditCardState(state)
                }
            }
        }
    }

    private fun handleCreditCardState(state: AppState<List<CreditCard>>) {
        when (state) {
            is AppState.Error -> {
                viewVisibility(binding.paymentMethodProgressBar, false)
                showMessage(state.message)
            }

            is AppState.Ideal -> Unit
            is AppState.Loading -> viewVisibility(binding.paymentMethodProgressBar, true)
            is AppState.Success -> {
                viewVisibility(binding.paymentMethodProgressBar, false)
                cardsAdapter.submitList(state.data!!)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchCreditCards()
        hideBottomNavigation()
    }
}
