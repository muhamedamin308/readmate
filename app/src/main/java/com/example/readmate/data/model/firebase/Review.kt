package com.example.readmate.data.model.firebase

/**
 * @author Muhamed Amin Hassan on 02,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class Review(
    val user: User? = null,
    val comment: String? = null,
    val timestamp: Long? = null
) {
    // Explicit no-argument constructor
    constructor() : this(null, null, null)
}