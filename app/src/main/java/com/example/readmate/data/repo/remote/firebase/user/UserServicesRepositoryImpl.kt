package com.example.readmate.data.repo.remote.firebase.user

import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.data.model.firebase.Notification
import com.example.readmate.data.service.remote.firebase.FirebaseUserService
import com.example.readmate.data.service.remote.firebase.FirestorePaymentService
import com.google.firebase.firestore.DocumentSnapshot

/**
 * @author Muhamed Amin Hassan on 11,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class UserServicesRepositoryImpl(
    private val userService: FirebaseUserService,
    private val paymentService: FirestorePaymentService,
) : UserServicesRepository {

    override fun getNotifications(onAction: (List<Notification>?, Exception?) -> Unit) {
        userService.getUserNotifications(onAction)
    }

    override fun saveNotification(notification: Notification) {
        userService.addUserNotification(notification)
    }

    override fun addCreditCard(creditCard: CreditCard) {
        paymentService.addPaymentMethod(creditCard)
    }

    override fun getAllCreditCards(onAction: (List<CreditCard>?, Exception?) -> Unit) {
        paymentService.getUserCreditCards(onAction)
    }

    override fun deleteCreditCard(
        creditCard: CreditCard
    ) {
        paymentService.removePaymentMethod(creditCard)
    }

    override fun checkForUniqueCreditCardNumber(cardNumber: String, onAction: (Boolean) -> Unit) {
        paymentService.checkForUniqueCreditCardNumber(cardNumber, onAction)
    }

    override fun addBookToBookcase(book: Book, onAction: (Book?, Exception?) -> Unit) {
        userService.addBookToUserBookCase(book, onAction)
    }

    override fun removeBookFromBookcase(bookIndex: Int?, bookDocument: List<DocumentSnapshot>) {
        userService.removeBookFromBookcase(bookIndex, bookDocument)
    }

    override fun removeBookFromBookcase(bookId: String, onAction: () -> Unit) {
        userService.removeBookFromBookcase(bookId, onAction)
    }

    override fun getBookcaseBooks(onAction: (List<Book>?, Exception?) -> Unit) {
        userService.fetchBookcase(onAction)
    }

    override fun buyNewBook(book: Book, onAction: (Book?, Exception?) -> Unit) {
        paymentService.buyBook(book, onAction)
    }

    override fun getMyBooks(onAction: (List<Book>?, Exception?) -> Unit) {
        userService.fetchBoughtBooks(onAction)
    }

    override fun isInBookcase(bookId: String, onAction: (Boolean) -> Unit) {
        userService.checkIfInBookcase(bookId, onAction)
    }

    override fun isInMyBooks(bookId: String, onAction: (Boolean) -> Unit) {
        userService.checkIfInMyBooks(bookId, onAction)
    }

    override fun applyPromoCode(code: String, onAction: (Float?, Exception?) -> Unit) {
        paymentService.applyPromoCode(code, onAction)
    }
}