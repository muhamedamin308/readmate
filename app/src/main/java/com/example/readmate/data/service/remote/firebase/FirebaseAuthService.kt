package com.example.readmate.data.service.remote.firebase

import android.content.Context
import com.example.readmate.data.model.firebase.User
import com.example.readmate.util.convertToUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

/**
 * @author Muhamed Amin Hassan on 10,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class FirebaseAuthService(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore,
    private val googleClient: GoogleSignInOptions,
    private val context: Context
) {
    private val userCollectionPath = store.collection("users")

    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(context, googleClient)
    }

    val isLoggedIn: Boolean = auth.currentUser != null

    fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onAction: (FirebaseUser?, Exception?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                it?.user?.let { user -> onAction(user, null) }
            }.addOnFailureListener { onAction(null, it) }
    }

    fun registerWithEmailAndPassword(
        user: User,
        password: String,
        onAction: (User?, Exception?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(user.email!!, password)
            .addOnSuccessListener {
                it?.user?.let { firebaseUser ->
                    saveUserData(firebaseUser.uid, user, onAction)
                }
            }.addOnFailureListener {
                onAction(null, it)
            }
    }

    fun getUserProfile(onAction: (User?, Exception?) -> Unit) {
        val userId = auth.currentUser?.uid
        userId?.let {
            userCollectionPath.document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    user?.let { onAction(user, null) } ?: onAction(
                        null,
                        Exception("User not found")
                    )
                }
                .addOnFailureListener { onAction(null, it) }
        } ?: onAction(null, Exception("Not authenticated user!"))
    }

    fun logout() {
        auth.signOut()
        googleSignInClient.signOut()
    }

    fun googleSignIn() = googleSignInClient.signInIntent

    fun firebaseAuthWithGoogle(idToken: String, onAction: (User?, Exception?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task -> handleFirebaseActions(task, onAction) }
    }

    private fun saveUserData(
        userId: String,
        user: User,
        onAction: (User?, Exception?) -> Unit
    ) {
        userCollectionPath.document(userId).set(user)
            .addOnSuccessListener { onAction(user, null) }
            .addOnFailureListener { onAction(null, it) }
    }

    private fun handleFirebaseActions(
        action: Task<AuthResult>,
        onAction: (User?, Exception?) -> Unit
    ) {
        action
            .addOnSuccessListener {
                val user = it.user
                user?.let {
                    saveUserData(user.uid, user.convertToUser(), onAction)
                    onAction(user.convertToUser(), null)
                } ?: onAction(
                    null,
                    Exception("Unexpected error!!")
                )
            }
            .addOnFailureListener { onAction(null, it) }
    }
}
