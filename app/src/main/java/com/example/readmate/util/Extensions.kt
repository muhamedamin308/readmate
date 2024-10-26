package com.example.readmate.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.example.readmate.data.model.firebase.ReviewedUser
import com.example.readmate.data.model.firebase.User
import com.google.firebase.auth.FirebaseUser

/**
 * @author Muhamed Amin Hassan on 20,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

fun FirebaseUser.toUser() = User(
    email = email,
    name = displayName,
    profileImage = photoUrl?.toString(),
    books = emptyList(),
    booksToRead = emptyList()
)

fun User.toReviewedUser() = ReviewedUser(
    email = email,
    name = name,
    profileImage = profileImage
)

fun Context.showMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun String.extractFetchRequestQuery(): String =
    this
        .lowercase()
        .replace("&", "and")
        .replace(Regex("\\s+"), "-")

fun String.extractSimilarBooksBasedOnName(): String =
    takeIf { it.isNotBlank() }?.split(" ")?.firstOrNull()?.lowercase() ?: ""

fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}
