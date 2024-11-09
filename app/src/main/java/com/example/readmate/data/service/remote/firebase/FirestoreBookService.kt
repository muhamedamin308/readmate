package com.example.readmate.data.service.remote.firebase

import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.model.firebase.Review
import com.example.readmate.util.Constants.CollectionPaths
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
    private val bookCollectionPath = store.collection(CollectionPaths.BOOKS)
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

    fun fetchBooksBySimilarity(
        currentBookRating: Float,
        ratingRange: Float = 0.5f,
        onAction: (List<Book>?, Exception?) -> Unit
    ) {
        val query = bookCollectionPath
            .whereGreaterThanOrEqualTo("averageRating", currentBookRating - ratingRange)
            .whereLessThanOrEqualTo("averageRating", currentBookRating + ratingRange)

        fetchBooks(query = query, limit = 8, onAction = onAction)
    }


    fun fetchAllBooks(onAction: (List<Book>?, Exception?) -> Unit) {
        fetchBooks(onAction = onAction)
    }

    fun addBookReview(
        bookId: String,
        review: Review
    ) {
        bookCollectionPath
            .whereEqualTo("bookId", bookId)
            .get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    it.documents.firstOrNull()?.reference?.let { document ->
                        document.collection(CollectionPaths.USER_BOOK_REVIEW)
                            .document()
                            .set(review)
                    }
                }
            }
    }

    fun fetchBookReviews(
        bookId: String,
        onAction: (List<Review>?, Exception?) -> Unit
    ) {
        bookCollectionPath
            .whereEqualTo("bookId", bookId)
            .get()
            .addOnSuccessListener {
                if (it.documents.isEmpty())
                    onAction(null, Exception("No documents!"))
                else {
                    it.documents.firstOrNull()?.reference?.let { document ->
                        document.collection(CollectionPaths.USER_BOOK_REVIEW)
                            .addSnapshotListener { querySnapshot, exception ->
                                if (exception != null) {
                                    onAction(null, exception)
                                    return@addSnapshotListener
                                }

                                querySnapshot?.documents?.let { listOfDocuments ->
                                    val reviews = listOfDocuments.mapNotNull { document ->
                                        document.toObject(Review::class.java)
                                    }
                                    onAction(reviews, null)
                                }
                            }
                    }
                }
            }
            .addOnFailureListener { onAction(null, it) }
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
