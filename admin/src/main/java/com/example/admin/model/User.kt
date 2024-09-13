package com.example.admin.model

/**
 * @author Muhamed Amin Hassan on 02,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class User(
    val uid: String?,
    val name: String?,
    val email: String?,
    val profileImage: String?,
    val isSubscribed: Boolean?,
    val books: List<String>?
)