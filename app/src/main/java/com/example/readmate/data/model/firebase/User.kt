package com.example.readmate.data.model.firebase

/**
 * @author Muhamed Amin Hassan on 02,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class User(
    val name: String? = null,
    val email: String? = null,
    val profileImage: String? = null,
    val books: List<String>? = emptyList(),
    val booksToRead: List<String> = emptyList()
)