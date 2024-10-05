package com.example.readmate.data.repo.remote.firebase.auth

import android.content.Intent
import android.net.Uri
import com.example.readmate.data.model.firebase.User
import com.example.readmate.data.service.remote.firebase.FirebaseAuthService
import com.example.readmate.data.service.remote.firebase.FirebaseUserService
import com.google.firebase.auth.FirebaseUser

class FirebaseUserRepositoryImpl(
    private val authService: FirebaseAuthService,
    private val userService: FirebaseUserService
) : FirebaseUserRepository {
    override fun register(user: User, password: String, onAction: (User?, Exception?) -> Unit) {
        authService.registerWithEmailAndPassword(user, password, onAction)
    }

    override fun login(
        email: String,
        password: String,
        onAction: (FirebaseUser?, Exception?) -> Unit
    ) {
        authService.loginWithEmailAndPassword(email, password, onAction)
    }

    override val isUserLoggedIn: Boolean
        get() = authService.isLoggedIn

    override fun signInWithGoogle(): Intent = authService.googleSignIn()

    override fun signOut() = authService.logout()

    override fun getUserProfile(onAction: (User?, Exception?) -> Unit) =
        userService.getUserProfile(onAction)

    override fun updateUserProfile(
        user: User,
        imageUri: Uri?,
        onAction: (User?, Exception?) -> Unit
    ) {
        userService.updateUserProfile(user, imageUri, onAction)
    }

    override fun authWithGoogle(token: String, onAction: (User?, Exception?) -> Unit) {
        authService.firebaseAuthWithGoogle(token, onAction)
    }

}