package com.example.readmate.data.source.remote.firebase

import com.example.readmate.data.model.firebase.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class FirebaseUserService(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore
) {
    private val userCollectionPath = store.collection("users")

    fun getUserProfile(onAction: (User?, Exception?) -> Unit) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    user?.let { onAction(it, null) } ?: onAction(
                        null,
                        Exception("User Profile Not Found!")
                    )
                }
                .addOnFailureListener { exception ->
                    onAction(null, exception)
                }
        } ?: onAction(null, Exception("Not Authenticated User"))
    }

    fun updateUserProfile() {

    }

}