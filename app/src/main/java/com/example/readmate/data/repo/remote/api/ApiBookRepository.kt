package com.example.readmate.data.repo.remote.api

import com.example.readmate.data.model.responses.BookDetailsResponse
import com.example.readmate.data.model.responses.BookResponse
import com.example.readmate.data.service.remote.api.ApiResult

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
interface ApiBookRepository {
    suspend fun getRecentBooks(): ApiResult<BookResponse>
    suspend fun searchBooks(query: String): ApiResult<BookResponse>
    suspend fun getBookDetails(bookId: String): BookDetailsResponse?
}