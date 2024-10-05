package com.example.readmate.data.service.remote.firebase

import com.example.readmate.data.model.firebase.Book
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class FirestoreBookService(
    store: FirebaseFirestore
) {
    private val bookCollectionPath = store.collection("books")
    private val numberOfReviews = "numberOfReviewers"

    fun fetchNewestBooks(onAction: (List<Book>?, Exception?) -> Unit) {
        fetchBooks(
            bookCollectionPath.orderBy("yearPublished", Query.Direction.DESCENDING),
            5,
            onAction
        )
    }

    fun fetchRecommendedBooks(onAction: (List<Book>?, Exception?) -> Unit) {
        fetchBooks(bookCollectionPath.orderBy("price", Query.Direction.DESCENDING), 10, onAction)
    }

    fun fetchBookCategories(onAction: (List<String>?, Exception?) -> Unit) {
        bookCollectionPath.get()
            .addOnSuccessListener {
                val categories = it.documents.mapNotNull { document ->
                    document.toObject(Book::class.java)?.categories
                }.flatten().toSet().toList()
                onAction(categories, null)
            }.addOnFailureListener { onAction(null, it) }
    }

    fun fetchBestSellersBooks(onAction: (List<Book>?, Exception?) -> Unit) {
        fetchBooks(
            bookCollectionPath.orderBy(numberOfReviews, Query.Direction.DESCENDING),
            8,
            onAction
        )
    }

    fun fetchTopRatedBooks(onAction: (List<Book>?, Exception?) -> Unit) {
        fetchBooks(
            bookCollectionPath
                .whereGreaterThanOrEqualTo(numberOfReviews, 300)
                .orderBy("averageRating", Query.Direction.DESCENDING),
            onAction = onAction,
            limit = 5
        )
    }

    fun fetchAllBooks(onAction: (List<Book>?, Exception?) -> Unit) {
        fetchBooks(onAction = onAction)
    }

    private fun fetchBooks(
        query: Query = bookCollectionPath,
        limit: Long? = null,
        onAction: (List<Book>?, Exception?) -> Unit
    ) {
        val finalQuery = limit?.let { query.limit(it) } ?: query
        finalQuery.get()
            .addOnSuccessListener {
                val books =
                    it.documents.mapNotNull { document -> document.toObject(Book::class.java) }
                onAction(books, null)
            }.addOnFailureListener { onAction(null, it) }
    }
}
