package com.example.readmate.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.model.firebase.MyBook
import com.example.readmate.data.model.firebase.ReviewedUser
import com.example.readmate.data.model.firebase.User
import com.example.readmate.data.model.local.BookState
import com.example.readmate.ui.main.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
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

fun Book.convertToMyBook(bookState: BookState) = MyBook(
    bookId = bookId,
    title = title,
    author = author,
    image = image,
    price = price,
    chapters = chapters,
    bookState = bookState
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

fun Fragment.hideBottomNavigation() {
    val navigationView =
        (activity as HomeActivity).findViewById<BottomNavigationView>(
            R.id.bottom_nav_view
        )
    navigationView.gone()
}

fun Fragment.showBottomNavigation() {
    val navigationView =
        (activity as HomeActivity).findViewById<BottomNavigationView>(
            R.id.bottom_nav_view
        )
    navigationView.show()
}