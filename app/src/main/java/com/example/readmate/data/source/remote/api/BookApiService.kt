package com.example.readmate.data.source.remote.api

import com.example.readmate.data.model.responses.BookDetailsResponse
import com.example.readmate.data.model.responses.BookResponse
import retrofit2.http.GET

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

interface BookApiService {
    @GET("")
    fun getAllBooks(): List<BookResponse>

    @GET("")
    fun getQueriedBooks(): List<BookResponse>

    @GET("")
    fun getBookDetails(): List<BookDetailsResponse>
}