package com.example.readmate.data.model.firebase

/**
 * @author Muhamed Amin Hassan on 02,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class Book(
    val image: String?,
    val title: String?,
    val year: String?,
    val subTitle: String?,
    val overview: String?,
    val author: String?,
    val pages: Int?,
    val rate: Int?,
    val numberOfRates: Int,
    val reviews: List<Review>,
    val yearPublished: Int?,
    val chapters: List<Chapter>,
    val price: Float?
)