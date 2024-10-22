package com.example.readmate.data.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Parcelize
data class Chapter(
    val title: String? = null,
    val headlines: List<HeadLine> = emptyList()
): Parcelable {
    // Explicit no-argument constructor
    constructor() : this(null, emptyList())
}