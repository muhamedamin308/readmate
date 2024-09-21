package com.example.readmate.data.source.remote.firebase

import com.example.readmate.data.model.firebase.Book

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class FirestoreBookService(
    // private val store: FirebaseFirestore
) {
    // TODO: create variable to save books collection location

    fun getFirebaseBooks(onAction: (Book, Exception) -> Unit) {
        TODO("connect this to the firestore to get all books")
    }

    fun getBookDetails(bookId: String, onAction: (Book, Exception) -> Unit) {
        TODO("get book details")
    }

    fun buyBook(bookId: String, onAction: (Book, Exception) -> Unit) {
        TODO("buy book")
    }
}