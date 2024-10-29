package com.example.readmate.data.model.firebase

import android.os.Parcelable
import com.example.readmate.data.model.local.BookState
import kotlinx.parcelize.Parcelize

/**
 * @author Muhamed Amin Hassan on 28,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Parcelize
data class MyBook(
    val bookId: String? = null,
    val image: String? = null,
    val title: String? = null,
    val author: String? = null,
    val chapters: List<Chapter>? = emptyList(),
    val bookState: BookState? = BookState.fromString("other")
) : Parcelable {
    constructor() : this(
        null, null, null, null, emptyList(), BookState.OTHER
    )
}
