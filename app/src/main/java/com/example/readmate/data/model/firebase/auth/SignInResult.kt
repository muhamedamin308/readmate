package com.example.readmate.data.model.firebase.auth

import com.example.readmate.data.model.firebase.User

data class SignInResult(
    val data: User?,
    val error: String? = null,
)
