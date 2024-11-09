package com.example.readmate.data.model.local

import android.annotation.SuppressLint
import java.math.RoundingMode

/**
 * @author Muhamed Amin Hassan on 28,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@SuppressLint("DefaultLocale")
enum class BookState(val discount: Float, private val tax: Float) {
    BEST_SELLER(0.12f, 0.08f),
    NEW(0.10f, 0.14f),
    RECOMMENDED(0.05f, 0.12f),
    HIGH_RATED(0.025f, 0.10f),
    OTHER(0.00f, 0.05f);

    companion object {
        fun fromString(state: String): BookState =
            entries.find { it.name.equals(state, ignoreCase = true) } ?: OTHER
    }

    fun getDiscountValue(price: Float): Float = (price * discount).toBigDecimal().setScale(
        2,
        RoundingMode.HALF_UP
    ).toFloat()

    fun getTaxValue(price: Float): Float = (price * tax).toBigDecimal().setScale(
        2,
        RoundingMode.HALF_UP
    ).toFloat()

    fun getTotalPrice(price: Float): Float {
        val priceAfterDiscount = price - getDiscountValue(price)
        val totalPrice = priceAfterDiscount + (priceAfterDiscount * tax)
        return totalPrice.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toFloat()
    }
}
