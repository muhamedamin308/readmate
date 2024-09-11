package com.example.readmate.data.model.firebase

data class BookId(val value: String) {
    init {
        require(value.isNotEmpty()) { "Book ID cannot be empty" }
    }

    override fun toString(): String = value
}