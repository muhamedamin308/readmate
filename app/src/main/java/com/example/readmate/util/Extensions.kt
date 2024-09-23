package com.example.readmate.util

import android.content.Context
import android.widget.Toast
import com.example.readmate.data.model.firebase.User
import com.google.firebase.auth.FirebaseUser

/**
 * @author Muhamed Amin Hassan on 20,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

fun FirebaseUser.convertToUser() = User(
    email = email,
    name = displayName,
    profileImage = photoUrl?.toString(),
    books = emptyList(),
    booksToRead = emptyList()
)

fun Context.showMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}