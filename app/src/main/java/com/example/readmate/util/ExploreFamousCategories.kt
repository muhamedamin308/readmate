package com.example.readmate.util

/**
 * @author Muhamed Amin Hassan on 16,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

val famousCategories = listOf(
    "Computer Science",
    "Science & Mathematics",
    "Economics & Finance",
    "Business & Management",
    "Politics & Government",
    "History",
    "Philosophy",
    "Action",
    "Adventure",
    "Comedy",
    "Drama",
    "Horror",
)

fun String.extractFetchRequestQuery(): String =
    this
        .lowercase()
        .replace("&", "and")
        .replace(Regex("\\s+"), "-")
