package com.example.readmate.data.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Muhamed Amin Hassan on 11,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Parcelize
data class HeadLine(
    val title: String? = null,
    val content: String? = null
) : Parcelable {
    // Explicit no-argument constructor
    constructor() : this(null, null)
}