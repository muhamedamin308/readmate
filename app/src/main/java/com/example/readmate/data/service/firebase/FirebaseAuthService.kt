package com.example.readmate.data.service.firebase

import com.google.firebase.auth.FirebaseAuth

/**
 * @author Muhamed Amin Hassan on 10,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class FirebaseAuthService(
    private val auth: FirebaseAuth
) {
    val isLoggedIn = auth.currentUser != null
}