package com.example.readmate.data.model.firebase

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class Chapter(
    val title: String,
    val headlines: List<HeadLine> = emptyList()
)
