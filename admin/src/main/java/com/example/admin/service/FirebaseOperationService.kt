package com.example.admin.service

import com.example.admin.model.Book
import com.google.firebase.firestore.FirebaseFirestore

/**
 * @author Muhamed Amin Hassan on 14,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class FirebaseOperationService(
    private val store: FirebaseFirestore
) {

    fun addBook(
        book: Book,
        onAction: (Book?, Exception?) -> Unit
    ) {
        store.collection("books")
            .document()
            .set(book)
            .addOnSuccessListener { onAction(book, null) }
            .addOnFailureListener { onAction(null, it) }
    }

    fun deleteBook(bookId: String, onAction: (String?, Exception?) -> Unit) {
        store.collection("books")
            .whereEqualTo("bookId", bookId)
            .get()
            .addOnSuccessListener {
                if (it.documents.isEmpty()) {
                    onAction("No book found with this ID", null)
                } else {
                    it.documents.firstOrNull()?.reference?.delete()
                        ?.addOnSuccessListener { onAction("Book with Id: $bookId Deleted!", null) }
                        ?.addOnFailureListener { exception -> onAction(null, exception) }
                }
            }
            .addOnFailureListener { onAction(null, it) }
    }
}