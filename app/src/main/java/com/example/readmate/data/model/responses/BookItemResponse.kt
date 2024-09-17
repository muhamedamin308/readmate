package com.example.readmate.data.model.responses

data class BookItemResponse(
    val authors: String,
    val id: String,
    val image: String,
    val subtitle: String,
    val title: String,
    val url: String
)