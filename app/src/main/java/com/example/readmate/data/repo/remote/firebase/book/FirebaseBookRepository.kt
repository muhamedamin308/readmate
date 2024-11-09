package com.example.readmate.data.repo.remote.firebase.book

import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.model.firebase.Review

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

interface FirebaseBookRepository {
    fun fetchNewestBooks(onAction: (List<Book>?, Exception?) -> Unit)
    fun fetchRecommendedBooks(onAction: (List<Book>?, Exception?) -> Unit)
    fun fetchBookCategories(onAction: (List<String>?, Exception?) -> Unit)
    fun fetchBestSellersBooks(onAction: (List<Book>?, Exception?) -> Unit)
    fun fetchTopRatedBooks(onAction: (List<Book>?, Exception?) -> Unit)
    fun fetchAllBooks(onAction: (List<Book>?, Exception?) -> Unit)
    fun fetchSimilarBooks(
        currentBookRating: Float,
        onAction: (List<Book>?, Exception?) -> Unit
    )
    fun addReview(
        bookId: String,
        review: Review
    )
    fun getBookReviews(
        bookId: String,
        onAction: (List<Review>?, Exception?) -> Unit
    )
}