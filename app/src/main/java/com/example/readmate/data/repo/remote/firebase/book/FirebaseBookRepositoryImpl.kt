package com.example.readmate.data.repo.remote.firebase.book

import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.service.remote.firebase.FirestoreBookService

/**
 * @author Muhamed Amin Hassan on 23,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class FirebaseBookRepositoryImpl(
    private val bookService: FirestoreBookService
) : FirebaseBookRepository {
    private fun <T> fetchFromService(
        fetch: (onAction: (T?, Exception?) -> Unit) -> Unit,
        onAction: (T?, Exception?) -> Unit
    ) {
        fetch(onAction)
    }

    override fun fetchNewestBooks(onAction: (List<Book>?, Exception?) -> Unit) =
        fetchFromService(bookService::fetchNewestBooks, onAction)

    override fun fetchRecommendedBooks(onAction: (List<Book>?, Exception?) -> Unit) =
        fetchFromService(bookService::fetchRecommendedBooks, onAction)

    override fun fetchBookCategories(onAction: (List<String>?, Exception?) -> Unit) =
        fetchFromService(bookService::fetchBookCategories, onAction)

    override fun fetchBestSellersBooks(onAction: (List<Book>?, Exception?) -> Unit) =
        fetchFromService(bookService::fetchBestSellersBooks, onAction)

    override fun fetchTopRatedBooks(onAction: (List<Book>?, Exception?) -> Unit) =
        fetchFromService(bookService::fetchTopRatedBooks, onAction)

    override fun fetchAllBooks(onAction: (List<Book>?, Exception?) -> Unit) =
        fetchFromService(bookService::fetchAllBooks, onAction)
}