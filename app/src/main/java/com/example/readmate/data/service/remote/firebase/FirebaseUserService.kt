package com.example.readmate.data.service.remote.firebase

import android.net.Uri
import com.example.readmate.data.model.firebase.User
import com.example.readmate.data.model.firebase.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class FirebaseUserService(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore,
    private val storage: StorageReference,
) {
    private val userCollectionPath = store.collection("users")

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

    fun updateUserProfile(
        user: User,
        imageUri: Uri?,
        onAction: (User?, Exception?) -> Unit,
    ) {
        if (imageUri == null) {
            saveUserInformation(user, true, onAction)
        } else {
            saveUserInformationWithNewImage(user, imageUri, onAction)
        }
    }

    fun saveUserNotification(
        notification: Notification,
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath
                .document(id)
                .collection("notifications")
                .document()
                .set(notification)
        }
    }

    fun getUserNotifications(
        onAction: (List<Notification>?, Exception?) -> Unit,
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id)
                .collection("notifications")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        onAction(null, exception)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val notifications =
                            snapshot.documents.mapNotNull { it.toObject(Notification::class.java) }
                        onAction(notifications, null)
                    } else
                        onAction(emptyList(), null)
                }
        } ?: run {
            onAction(null, Exception("User not logged in!"))
        }
    }

    private fun saveUserInformationWithNewImage(
        user: User,
        imageUri: Uri,
        onAction: (User?, Exception?) -> Unit,
    ) {
        val imageRef = storage.child("profile_images/${imageUri.lastPathSegment}")
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    saveUserInformation(
                        user.copy(profileImage = downloadUrl.toString()),
                        false,
                        onAction
                    )
                }
            }
            .addOnFailureListener { onAction(null, it) }
    }

    private fun saveUserInformation(
        user: User,
        retrieveOldImage: Boolean,
        onAction: (User?, Exception?) -> Unit,
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            store.runTransaction { transaction ->
                val documentRef = userCollectionPath.document(id)
                if (retrieveOldImage) {
                    val currentUser = transaction.get(documentRef).toObject(User::class.java)
                    val newUser = user.copy(profileImage = currentUser?.profileImage ?: "")
                    transaction.set(documentRef, newUser)
                } else {
                    transaction.set(documentRef, user)
                }
            }
                .addOnSuccessListener { onAction(user, null) }
                .addOnFailureListener { onAction(null, it) }
        }
    }
}
