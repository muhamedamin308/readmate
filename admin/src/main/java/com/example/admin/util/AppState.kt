package com.example.admin.util

/**
 * @author Muhamed Amin Hassan on 14,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

sealed class AppState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : AppState<T>(data)
    class Error<T>(message: String) : AppState<T>(message = message)
    class Loading<T> : AppState<T>()
    class Ideal<T> : AppState<T>()
}