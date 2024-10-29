package com.example.readmate.data.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Muhamed Amin Hassan on 02,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Parcelize
data class CreditCard(
    val cardNumber: String? = null,
    val expirationDate: String? = null,
    val cvv: String? = null,
    val cardHolderName: String? = null
) : Parcelable
