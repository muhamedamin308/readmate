package com.example.readmate.ui.payment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.data.repo.remote.firebase.user.UserServicesRepository
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class PaymentViewModel(private val userServicesRepository: UserServicesRepository) : ViewModel() {

    private val _newCreditCard = MutableStateFlow<AppState<CreditCard>>(AppState.Ideal())
    val newCreditCard = _newCreditCard.asStateFlow()

    private val _allCreditCards = MutableStateFlow<AppState<List<CreditCard>>>(AppState.Ideal())
    val allCreditCards = _allCreditCards.asStateFlow()

    private val _removeCreditCard = MutableStateFlow<AppState<Boolean>>(AppState.Ideal())
    val removeCreditCard = _removeCreditCard.asStateFlow()


    fun addCreditCard(creditCard: CreditCard) {
        if (isValidCreditCard(creditCard)) {
            updateAppState(_newCreditCard, AppState.Loading())
            userServicesRepository.addCreditCard(creditCard) { newCard, exception ->
                handleResult(_newCreditCard, newCard, exception)
            }
        } else {
            updateAppState(_newCreditCard, AppState.Error("Invalid credit card details"))
        }
    }

    fun removeCreditCard(creditCard: CreditCard) {
        updateAppState(_removeCreditCard, AppState.Loading())
        userServicesRepository.deleteCreditCard(creditCard) { isCardRemoved, exception ->
            handleResult(_removeCreditCard, isCardRemoved, exception)
        }
    }

    fun fetchCreditCards() {
        updateAppState(_allCreditCards, AppState.Loading())
        userServicesRepository.getAllCreditCards { creditCards, exception ->
            handleResult(_allCreditCards, creditCards, exception)
        }
    }

    private fun <T> handleResult(
        stateFlow: MutableStateFlow<AppState<T>>,
        result: T?,
        exception: Exception?
    ) {
        exception?.let {
            updateAppState(stateFlow, AppState.Error(it.message ?: "An error occurred"))
        } ?: updateAppState(stateFlow, AppState.Success(result!!))
    }

    private fun <T> updateAppState(
        stateFlow: MutableStateFlow<AppState<T>>,
        newState: AppState<T>
    ) {
        viewModelScope.launch { stateFlow.emit(newState) }
    }

    private val isValidCreditCard: (CreditCard) -> Boolean = {
        it.cardNumber.length == 16 &&
                it.cardNumber.trim().isNotEmpty() &&
                it.cardHolderName.trim().isNotEmpty() &&
                it.expirationDate.trim().isNotEmpty() &&
                it.cvv.length == 3 &&
                it.cvv.trim().isNotEmpty()
    }
}
