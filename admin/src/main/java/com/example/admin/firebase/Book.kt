package com.example.readmate.data.model.firebase

/**
 * @author Muhamed Amin Hassan on 02,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class Book(
    val id: BookId,
    val image: String?,
    val title: String,
    val author: String,
    val subTitle: String?,
    val overview: String?,
    val yearPublished: Int,
    val numberOfPages: Int,
    val rating: Rating = Rating(),
    val reviews: List<Review> = emptyList(),
    val chapters: List<Chapter> = emptyList(),
    val price: Float?
)
