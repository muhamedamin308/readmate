package com.example.readmate.data.service.remote.api

import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    ApiResult.Success(it)
                } ?: ApiResult.Error("No data found!")
            } else {
                val message = when (response.code()) {
                    404 -> "Resource not found"
                    500 -> "Server error, try again later"
                    else -> "Error Code: ${response.code()} - ${response.message()}"
                }
                ApiResult.Error(message)
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "An unknown error occurred")
        }
    }
}