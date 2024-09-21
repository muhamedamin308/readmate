package com.example.readmate.data.repo.remote.firebase

import android.content.Intent
import com.example.readmate.data.model.firebase.User
import com.example.readmate.data.source.remote.firebase.FirebaseAuthService

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
interface FirebaseUserRepository {
    fun register(user: User, password: String, onAction: (User?, Exception?) -> Unit)
    fun login(email: String, password: String, onAction: (User?, Exception?) -> Unit)
    val isUserLoggedIn: Boolean
    fun signInWithGoogle(): Intent
    fun authWithGoogle(token: String, onAction: (User?, Exception?) -> Unit)
}


class FirebaseUserRepositoryImpl(
    private val authService: FirebaseAuthService
) : FirebaseUserRepository {
    override fun register(user: User, password: String, onAction: (User?, Exception?) -> Unit) {
        authService.registerWithEmailAndPassword(user, password, onAction)
    }

    override fun login(email: String, password: String, onAction: (User?, Exception?) -> Unit) {
        authService.loginWithEmailAndPassword(email, password, onAction)
    }

    override val isUserLoggedIn: Boolean
        get() = authService.isLoggedIn

    override fun signInWithGoogle(): Intent = authService.googleSignIn()

    override fun authWithGoogle(token: String, onAction: (User?, Exception?) -> Unit) {
        authService.firebaseAuthWithGoogle(token, onAction)
    }
}