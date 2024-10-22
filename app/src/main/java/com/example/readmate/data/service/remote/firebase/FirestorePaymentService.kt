package com.example.readmate.data.service.remote.firebase

import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.util.Constants.CollectionPaths
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
        book: Book,
        onAction: (Book?, Exception?) -> Unit
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

    fun addPaymentMethod(
        creditCard: CreditCard,
        onAction: (CreditCard?, Exception?) -> Unit,
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath
                .document(id)
                .collection(CollectionPaths.USER_CREDIT_CARDS)
                .document()
                .set(creditCard)
                .addOnSuccessListener { onAction(creditCard, null) }
                .addOnFailureListener { onAction(null, it) }
        } ?: run { onAction(null, Exception("Not Authenticated User!")) }
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

    fun removePaymentMethod(
        creditCard: CreditCard,
        onAction: (Boolean, Exception?) -> Unit,
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id).collection(CollectionPaths.USER_CREDIT_CARDS)
                .whereEqualTo("cardNumber", creditCard.cardNumber)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.documents.firstOrNull()?.let { document ->
                        document.reference.delete()
                            .addOnSuccessListener { onAction(true, null) }
                            .addOnFailureListener { onAction(false, it) }
                    } ?: run {
                        onAction(false, Exception("Card not found!"))
                    }
                }
        } ?: run { onAction(false, Exception("Not authenticated User!")) }
    }
}