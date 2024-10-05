package com.example.readmate.data.model.responses

data class BookResponse(
    val books: List<BookItemResponse>?,
    val status: String?,
    val total: Int?
)