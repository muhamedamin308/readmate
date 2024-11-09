package com.example.readmate.data.model.local

/**
 * @author Muhamed Amin Hassan on 09,November,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class PromoCode(
    val code: String,
    val discountPercentage: Float,
    var isUsed: Boolean = false
)
