package com.example.readmate.ui.payment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readmate.R
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.data.model.firebase.MyBook
import com.example.readmate.databinding.FragmentPaymentMethodsBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.dialog.addNewCreditCardDialog
import com.example.readmate.ui.dialog.customAlertDialog
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
    private val navArgs by navArgs<PaymentMethodsFragmentArgs>()
    private val viewModel by viewModel<PaymentViewModel>()
    private val cardsAdapter by lazy { CreditCardsAdapter(binding.containerEmptyList) }
    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentPaymentMethodsBinding =
        FragmentPaymentMethodsBinding.inflate(layoutInflater)

    private lateinit var book: MyBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = navArgs.myBook
    }

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

        cardsAdapter.onClick = { creditCard ->
            navigateToPayment(book, creditCard)
        }
    }

    private fun setupAddPaymentButton() {
        binding.addPayment.setOnClickListener {
            addNewCreditCardDialog { creditCard ->
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

    private fun navigateToPayment(book: MyBook, creditCard: CreditCard) {
        findNavController().navigate(
            R.id.action_paymentMethodFragment_to_paymentFragment,
            Bundle().apply {
                putParcelable("myBook", book)
                putParcelable("creditCard", creditCard)
            }
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchCreditCards()
    }
}
