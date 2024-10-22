package com.example.readmate.data.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Muhamed Amin Hassan on 02,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Parcelize
data class User(
    var name: String? = null,
    val email: String? = null,
    var profileImage: String? = null,
    var books: List<String>? = emptyList(),
    var booksToRead: List<String> = emptyList()
): Parcelable {
    constructor() : this(null, null, null, emptyList(), emptyList())
}