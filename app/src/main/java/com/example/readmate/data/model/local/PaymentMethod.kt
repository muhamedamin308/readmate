package com.example.readmate.data.model.local

/**
 * @author Muhamed Amin Hassan on 02,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class PaymentMethod(
    val cardNumber: Int,
    val expirationDate: String,
    val cvv: Int,
    val cardHolderName: String
)
