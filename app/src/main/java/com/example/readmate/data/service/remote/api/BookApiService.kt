package com.example.readmate.data.service.remote.api

import com.example.readmate.data.model.responses.BookDetailsResponse
import com.example.readmate.data.model.responses.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

interface BookApiService {
    @GET("recent")
    suspend fun getRecentBooks(): Response<BookResponse>

    @GET("search/{query}")
    suspend fun searchBooks(@Path("query") query: String): Response<BookResponse>

    @GET("book/{id}")
    suspend fun getBookById(@Path("id") id: String): BookDetailsResponse
}