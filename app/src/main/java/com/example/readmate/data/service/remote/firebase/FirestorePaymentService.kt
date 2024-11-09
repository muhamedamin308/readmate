package com.example.readmate.data.service.remote.firebase

import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.data.model.firebase.MyBook
import com.example.readmate.util.Constants.CollectionPaths
import com.example.readmate.util.availablePromoCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class FirestorePaymentService(
    private val store: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    private val userCollectionPath = store.collection(CollectionPaths.USERS)

    fun buyBook(
        book: MyBook,
        onAction: (MyBook?, Exception?) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id)
                .update("books", FieldValue.arrayUnion(book.bookId))
            userCollectionPath.document(id)
                .collection(CollectionPaths.USER_MY_BOOKS)
                .document()
                .set(book)
                .addOnSuccessListener { onAction(book, null) }
                .addOnFailureListener { onAction(null, it) }
        }
    }

    fun addCreditCard(
        creditCard: CreditCard,
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath
                .document(id)
                .collection(CollectionPaths.USER_CREDIT_CARDS)
                .document()
                .set(creditCard)
        }
    }

    fun getUserCreditCards(
        onAction: (List<CreditCard>?, Exception?) -> Unit,
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id).collection(CollectionPaths.USER_CREDIT_CARDS)
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        onAction(null, exception)
                        return@addSnapshotListener
                    }

                    querySnapshot?.documents?.let {
                        val creditCards = it.mapNotNull { document ->
                            document.toObject(CreditCard::class.java)
                        }
                        onAction(creditCards, null)
                    }
                }
        } ?: run {
            onAction(null, Exception("User not logged in!"))
        }
    }

    fun removeCreditCard(
        creditCard: CreditCard
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id).collection(CollectionPaths.USER_CREDIT_CARDS)
                .whereEqualTo("cardNumber", creditCard.cardNumber)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.documents.firstOrNull()?.reference?.delete()
                }
        }
    }

    fun checkForUniqueCreditCardNumber(
        cardNumber: String,
        onAction: (Boolean) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id).collection(CollectionPaths.USER_CREDIT_CARDS)
                .whereEqualTo("cardNumber", cardNumber)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val exists = !querySnapshot.isEmpty
                    onAction(exists)
                }
                .addOnFailureListener { onAction(false) }
        }
    }

    fun applyPromoCode(
        code: String,
        onAction: (Float?, Exception?) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id)
                .collection("promoCodes")
                .document(code)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists() && document.getBoolean("used") == true) {
                        onAction(null, Exception("Invalid or already used promo code."))
                    } else {
                        val promoCode = availablePromoCodes.find {
                            it.code.equals(code, ignoreCase = true)
                        }
                        promoCode?.let {
                            userCollectionPath.document(id)
                                .collection("promoCodes")
                                .document(code)
                                .set(mapOf("used" to true))
                                .addOnSuccessListener {
                                    onAction(promoCode.discountPercentage, null)
                                }
                                .addOnFailureListener {
                                    onAction(null, it)
                                }
                        } ?: onAction(null, Exception("Promo code not found."))
                    }
                }
                .addOnFailureListener {
                    onAction(null, it)
                }
        } ?: onAction(null, Exception("User not authenticated."))
    }
}