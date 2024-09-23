package com.example.readmate.data.repo.remote.firebase.auth

import android.content.Intent
import com.example.readmate.data.model.firebase.User
import com.google.firebase.auth.FirebaseUser

interface FirebaseUserRepository {
    fun register(user: User, password: String, onAction: (User?, Exception?) -> Unit)
    fun login(email: String, password: String, onAction: (FirebaseUser?, Exception?) -> Unit)
    val isUserLoggedIn: Boolean
    fun signInWithGoogle(): Intent
    fun authWithGoogle(token: String, onAction: (User?, Exception?) -> Unit)
}