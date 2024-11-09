package com.example.readmate.data.service.remote.firebase

import android.net.Uri
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.model.firebase.MyBook
import com.example.readmate.data.model.firebase.Notification
import com.example.readmate.data.model.firebase.User
import com.example.readmate.util.Constants.CollectionPaths
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
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
    private val userCollectionPath = store.collection(CollectionPaths.USERS)

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

    fun addUserNotification(
        notification: Notification,
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath
                .document(id)
                .collection(CollectionPaths.NOTIFICATIONS)
                .document()
                .set(notification)
        }
    }

    fun getUserNotifications(
        onAction: (List<Notification>?, Exception?) -> Unit,
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id).collection(CollectionPaths.NOTIFICATIONS)
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        onAction(null, exception)
                        return@addSnapshotListener
                    }

                    querySnapshot?.documents?.let {
                        val notifications = it.mapNotNull { document ->
                            document.toObject(Notification::class.java)
                        }
                        onAction(notifications, null)
                    }
                }
        } ?: run {
            onAction(null, Exception("User not logged in!"))
        }
    }

    fun addBookToUserBookCase(
        book: Book,
        onAction: (Book?, Exception?) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id)
                .update("booksToRead", FieldValue.arrayUnion(book.bookId))
            userCollectionPath.document(id)
                .collection(CollectionPaths.USER_BOOKCASE)
                .document()
                .set(book)
                .addOnSuccessListener { onAction(book, null) }
                .addOnFailureListener { onAction(null, it) }
        }
    }

    fun removeBookFromBookcase(
        bookId: String,
        onAction: () -> Unit
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id)
                .update("booksToRead", FieldValue.arrayRemove(bookId))
            userCollectionPath.document(id)
                .collection(CollectionPaths.USER_BOOKCASE)
                .whereEqualTo("bookId", bookId)
                .get()
                .addOnSuccessListener {
                    it.documents.firstOrNull()?.reference?.delete()
                        ?.addOnSuccessListener { onAction() }
                        ?.addOnFailureListener { onAction() }
                }
                .addOnFailureListener { onAction() }
        }
    }

    fun removeBookFromBookcase(
        bookIndex: Int?,
        bookDocument: List<DocumentSnapshot>
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            if (bookIndex != null && bookIndex != -1) {
                userCollectionPath.document(id)
                    .update("booksToRead", FieldValue.arrayRemove(bookDocument[bookIndex].id))
                userCollectionPath.document(id)
                    .collection(CollectionPaths.USER_BOOKCASE)
                    .document(bookDocument[bookIndex].id)
                    .delete()
            }
        }
    }

    fun fetchBookcase(
        onAction: (List<Book>?, Exception?) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id).collection(CollectionPaths.USER_BOOKCASE)
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        onAction(null, exception)
                        return@addSnapshotListener
                    }

                    querySnapshot?.documents?.let {
                        val books = it.mapNotNull { document ->
                            document.toObject(Book::class.java)
                        }
                        onAction(books, null)
                    }
                }
        } ?: run {
            onAction(null, Exception("User not logged in!"))
        }
    }

    fun fetchBoughtBooks(
        onAction: (List<MyBook>?, Exception?) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id)
                .collection(CollectionPaths.USER_MY_BOOKS)
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        onAction(null, exception)
                        return@addSnapshotListener
                    }

                    querySnapshot?.documents?.let {
                        val books = it.mapNotNull { document ->
                            document.toObject(MyBook::class.java)
                        }
                        onAction(books, null)
                    }
                }
        } ?: run {
            onAction(null, Exception("User not logged in!"))
        }
    }

    fun checkIfInBookcase(
        bookId: String,
        onAction: (Boolean) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    user?.booksToRead?.contains(bookId)?.let { onAction(it) }
                        ?: onAction(false)
                }
                .addOnFailureListener { onAction(false) }
        }
    }

    fun checkIfInMyBooks(
        bookId: String,
        onAction: (Boolean) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        userId?.let { id ->
            userCollectionPath.document(id)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    user?.books?.contains(bookId)?.let { onAction(it) }
                        ?: onAction(false)
                }
                .addOnFailureListener { onAction(false) }
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
