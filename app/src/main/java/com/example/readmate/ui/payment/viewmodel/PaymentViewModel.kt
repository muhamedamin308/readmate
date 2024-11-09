package com.example.readmate.ui.payment.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.data.model.firebase.MyBook
import com.example.readmate.data.model.local.DiscountState
import com.example.readmate.data.repo.remote.firebase.user.UserServicesRepository
import com.example.readmate.ui.base.BaseViewModel
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class PaymentViewModel(
    private val userServicesRepository: UserServicesRepository
) : BaseViewModel() {

    private val _allCreditCards = MutableStateFlow<AppState<List<CreditCard>>>(AppState.Ideal())
    val allCreditCards = _allCreditCards.asStateFlow()

    private val _buyNewBook = MutableStateFlow<AppState<MyBook>>(AppState.Ideal())
    val buyNewBook = _buyNewBook.asStateFlow()

    private val _discountPercentage = MutableStateFlow<AppState<DiscountState>>(AppState.Ideal())
    val discountPercentage = _discountPercentage.asStateFlow()

    fun addCreditCard(creditCard: CreditCard) {
        userServicesRepository.addCreditCard(creditCard)
    }

    fun removeCreditCard(creditCard: CreditCard) {
        userServicesRepository.deleteCreditCard(creditCard)
    }

    fun checkForUniqueCreditCardNumber(cardNumber: String, onResult: (Boolean) -> Unit) {
        userServicesRepository.checkForUniqueCreditCardNumber(cardNumber, onResult)
    }

    fun fetchCreditCards() {
        updateAppState(_allCreditCards, AppState.Loading())
        userServicesRepository.getAllCreditCards { creditCards, exception ->
            handleResult(_allCreditCards, creditCards, exception)
        }
    }

    fun applyPromoCode(promoCode: String) {
        viewModelScope.launch { _discountPercentage.emit(AppState.Loading()) }
        userServicesRepository.applyPromoCode(promoCode) { discount, error ->
            viewModelScope.launch {
                if (error != null) {
                    _discountPercentage.emit(AppState.Error(error.message!!))
                } else {
                    discount?.let {
                        _discountPercentage.emit(
                            AppState.Success(
                                DiscountState(
                                    it,
                                    "Promo code applied: -${(it * 100).toInt()}%",
                                    isValid = true
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun buyBook(book: MyBook) {
        updateAppState(_buyNewBook, AppState.Loading())
        userServicesRepository.buyNewBook(book) { newBook, error ->
            handleResult(_buyNewBook, newBook, error)
        }
    }

    fun isValidCreditCard(creditCard: CreditCard): Boolean =
        creditCard.cardNumber?.length == 16 &&
                creditCard.cardNumber.trim().isNotEmpty() &&
                creditCard.cardHolderName?.trim()!!.isNotEmpty() &&
                creditCard.expirationDate?.trim()!!.isNotEmpty() &&
                creditCard.cvv?.length == 3 &&
                creditCard.cvv.trim().isNotEmpty()
}
