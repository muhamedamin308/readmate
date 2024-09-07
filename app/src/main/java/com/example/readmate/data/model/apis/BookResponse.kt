package com.example.readmate.data.model.apis

data class BookResponse(
    val books: List<BookItemResponse>,
    val status: String,
    val total: Int
)