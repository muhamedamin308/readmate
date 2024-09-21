package com.example.readmate.util

sealed class AppState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<Q>(data: Q) : AppState<Q>(data)
    class Error<Q>(message: String) : AppState<Q>(message = message)
    class Loading<Q> : AppState<Q>()
    class Ideal<Q> : AppState<Q>()
}