package com.example.readmate.data.repo.remote.firebase.user

import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.data.model.firebase.Notification
import com.google.firebase.firestore.DocumentSnapshot

/**
 * @author Muhamed Amin Hassan on 11,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
interface UserServicesRepository {
    fun getNotifications(
        onAction: (List<Notification>?, Exception?) -> Unit,
    )

    fun saveNotification(notification: Notification)

    fun addCreditCard(creditCard: CreditCard)

    fun getAllCreditCards(
        onAction: (List<CreditCard>?, Exception?) -> Unit,
    )

    fun deleteCreditCard(
        creditCard: CreditCard
    )

    fun checkForUniqueCreditCardNumber(
        cardNumber: String,
        onAction: (Boolean) -> Unit
    )

    fun addBookToBookcase(
        book: Book,
        onAction: (Book?, Exception?) -> Unit
    )

    fun removeBookFromBookcase(
        bookIndex: Int?,
        bookDocument: List<DocumentSnapshot>
    )

    fun removeBookFromBookcase(bookId: String, onAction: () -> Unit)

    fun getBookcaseBooks(
        onAction: (List<Book>?, Exception?) -> Unit
    )

    fun buyNewBook(
        book: Book,
        onAction: (Book?, Exception?) -> Unit
    )

    fun getMyBooks(
        onAction: (List<Book>?, Exception?) -> Unit
    )

    fun isInBookcase(bookId: String, onAction: (Boolean) -> Unit)
    fun isInMyBooks(bookId: String, onAction: (Boolean) -> Unit)

    fun applyPromoCode(
        code: String,
        onAction: (Float?, Exception?) -> Unit
    )
}