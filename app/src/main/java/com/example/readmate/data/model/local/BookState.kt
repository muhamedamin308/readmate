package com.example.readmate.data.model.local

/**
 * @author Muhamed Amin Hassan on 28,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

enum class BookState(private val discount: Float, private val tax: Float) {
    BEST_SELLER(0.12f, 0.08f),
    NEW(0.10f, 0.14f),
    RECOMMENDED(0.05f, 0.12f),
    HIGH_RATED(0.025f, 0.10f),
    OTHER(0.00f, 0.05f);

    companion object {
        fun fromString(state: String): BookState? =
            entries.find { it.name.equals(state, ignoreCase = true) }
    }

    fun getDiscountValue(state: String): Float =
        fromString(state)?.discount ?: OTHER.discount

    fun getTaxVale(state: String): Float =
        fromString(state)?.tax ?: OTHER.tax
}
