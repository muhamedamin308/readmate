package com.example.readmate.util

import android.content.Context
import com.example.readmate.R

fun generateRandomColor(context: Context): Int {
    val colors = listOf(
        R.color.randomColor1,
        R.color.randomColor2,
        R.color.randomColor3,
        R.color.randomColor4,
        R.color.randomColor5,
    )
    val randomIndex = colors.indices.random()
    return context.getColor(colors[randomIndex])
}