package com.example.readmate.data.model.firebase

/**
 * @author Muhamed Amin Hassan on 02,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class User(
    val name: String?,
    val email: String?,
    val profileImage: String?,
    val books: List<String>?,
    val booksToRead: List<String>?
)