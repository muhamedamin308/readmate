package com.example.readmate.data.service.remote.firebase

import android.content.Context
import com.example.readmate.data.model.firebase.User
import com.example.readmate.util.Constants
import com.example.readmate.util.toUser
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
    store: FirebaseFirestore,
    private val googleClient: GoogleSignInOptions,
    private val context: Context
) {
    private val userCollectionPath = store.collection(Constants.CollectionPaths.USERS)

    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(context, googleClient)
    }

    val isLoggedIn: Boolean
        get() = auth.currentUser != null

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

    fun logout(onLogoutComplete: (Boolean) -> Unit) {
        auth.signOut()
        googleSignInClient.signOut()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onLogoutComplete(true)
                } else {
                    onLogoutComplete(false)
                }
            }
    }

    fun googleSignIn() = googleSignInClient.signInIntent

    fun firebaseAuthWithGoogle(idToken: String, onAction: (User?, Exception?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    firebaseUser?.let { user ->
                        userCollectionPath.document(user.uid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val customUser = document.toObject(User::class.java)
                                    onAction(customUser, null) // Use Firestore data
                                } else {
                                    val newUser = user.toUser()
                                    saveUserData(user.uid, newUser, onAction)
                                }
                            }
                            .addOnFailureListener { exception ->
                                onAction(null, exception)
                            }
                    } ?: onAction(null, Exception("User is null"))
                } else {
                    onAction(null, task.exception)
                }
            }
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
                    saveUserData(user.uid, user.toUser(), onAction)
                    onAction(user.toUser(), null)
                } ?: onAction(
                    null,
                    Exception("Unexpected error!!")
                )
            }
            .addOnFailureListener { onAction(null, it) }
    }
}
