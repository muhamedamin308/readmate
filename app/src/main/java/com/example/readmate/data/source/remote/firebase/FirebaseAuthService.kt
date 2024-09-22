package com.example.readmate.data.source.remote.firebase

import android.content.Context
import com.example.readmate.data.model.firebase.User
import com.example.readmate.util.convertToUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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
        onAction: (User?, Exception?) -> Unit
    ) {
        handleFirebaseActions(auth.signInWithEmailAndPassword(email, password), onAction)
    }

    fun registerWithEmailAndPassword(
        user: User,
        password: String,
        onAction: (User?, Exception?) -> Unit
    ) {
        handleFirebaseActions(
            auth.createUserWithEmailAndPassword(user.email!!, password),
            onAction,
            saveUser = true,
            user = user
        )
    }

    fun googleSignIn() = googleSignInClient.signInIntent

    fun firebaseAuthWithGoogle(idToken: String, onAction: (User?, Exception?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task -> handleFirebaseActions(task, onAction) }
    }

    private fun saveUserData(user: User, onAction: (User?, Exception?) -> Unit) {
        userCollectionPath.document(user.uid!!).set(user)
            .addOnSuccessListener {
                onAction(user, null)
            }
            .addOnFailureListener {
                onAction(null, it)
            }
    }

    private fun handleFirebaseActions(
        action: Task<AuthResult>,
        onAction: (User?, Exception?) -> Unit,
        saveUser: Boolean = false,
        user: User? = null
    ) {
        action
            .addOnSuccessListener {
                val firebaseUser = it.user?.convertToUser()
                if (saveUser && firebaseUser != null && user != null)
                    saveUserData(user, onAction)
                else onAction(firebaseUser, null)
            }
            .addOnFailureListener { onAction(null, it) }
    }
}
