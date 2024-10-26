package com.example.readmate.data.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Muhamed Amin Hassan on 26,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Parcelize
data class ReviewedUser(
    var name: String? = null,
    val email: String? = null,
    var profileImage: String? = null,
) : Parcelable {
    constructor() : this(null, null, null)
}
