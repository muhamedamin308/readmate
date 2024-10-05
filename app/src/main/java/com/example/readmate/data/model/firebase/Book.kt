package com.example.readmate.data.model.firebase

/**
 * @author Muhamed Amin Hassan on 02,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class Book(
    val bookId: String? = null,
    val image: String? = null,
    val title: String? = null,
    val author: String? = null,
    val subTitle: String? = null,
    val overview: String? = null,
    val yearPublished: Int? = null,
    val numberOfPages: Int? = null,
    val averageRating: Float? = null,
    val numberOfReviewers: Int? = null,
    val categories: List<String> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val chapters: List<Chapter> = emptyList(),
    val price: Float? = null
) {
    // Explicit no-argument constructor for Firebase
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        emptyList(),
        emptyList(),
        emptyList(),
        null
    )
}