package com.example.readmate.util

import com.example.readmate.R

object Constants {
    const val SHARED_PREFERENCES_NAME = "ReadMateName"
    const val SHARED_PREFERENCES_KEY = "ReadMateKey"
    val signInPath = R.id.action_splashFragment_to_signInFragment
    const val HOME_ACTIVITY_ID = 4723
    const val GOOGLE_SIGN_IN_REQUEST = 22
    const val CLICKED_BOOK = "c"

    data object CollectionPaths {
        const val BOOKS = "books"
        const val USERS = "users"
        const val NOTIFICATIONS = "notifications"
        const val USER_BOOKCASE = "bookcase"
        const val USER_MY_BOOKS = "myBooks"
        const val USER_CREDIT_CARDS = "creditCards"
        const val USER_BOOK_REVIEW = "review"
    }

}