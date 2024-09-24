package com.example.readmate.data.repo.remote.api

import com.example.readmate.data.model.responses.BookDetailsResponse
import com.example.readmate.data.model.responses.BookResponse
import com.example.readmate.data.service.remote.api.ApiResult
import com.example.readmate.data.service.remote.api.BookApiService
import com.example.readmate.data.service.remote.api.SafeApiRequest

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class ApiBookRepositoryImpl(
    private val apiService: BookApiService
) : ApiBookRepository, SafeApiRequest() {

    override suspend fun getRecentBooks(): ApiResult<BookResponse> {
        return apiRequest { apiService.getRecentBooks() }
    }

    override suspend fun searchBooks(query: String): ApiResult<BookResponse> {
        return apiRequest { apiService.searchBooks(query) }
    }

    override suspend fun getBookDetails(bookId: String): BookDetailsResponse =
        apiService.getBookById(bookId)
}