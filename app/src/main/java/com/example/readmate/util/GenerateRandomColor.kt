package com.example.readmate.util

import android.content.Context
import com.example.readmate.R

private val colorMap = mutableMapOf<Int, Int>()

fun generateRandomColor(context: Context, position: Int): Int {
    return colorMap.getOrPut(position) {
        val colors = listOf(
            R.color.randomColor1,
            R.color.randomColor2,
            R.color.randomColor3,
            R.color.randomColor4,
            R.color.randomColor5,
        )
        val randomIndex = colors.indices.random()
        context.getColor(colors[randomIndex])
    }
}