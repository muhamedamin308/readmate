package com.example.readmate.di

import android.app.Application
import android.content.SharedPreferences
import com.example.readmate.R
import com.example.readmate.data.source.remote.firebase.FirebaseAuthService
import com.example.readmate.util.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * @author Muhamed Amin Hassan on 10,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { Firebase.firestore }
    single {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(androidContext().getString(R.string.default_web_client))
            .requestEmail()
            .build()
    }

    single {
        GoogleSignIn.getClient(androidContext(), get<GoogleSignInOptions>())
    }

    single<SharedPreferences> {
        val application: Application = get()
        application.getSharedPreferences(
            Constants.SHARED_PREFERENCES_NAME,
            Application.MODE_PRIVATE
        )
    }

    single {
        FirebaseAuthService(
            auth = get(),
            store = get(),
            googleClient = get(),
            androidContext()
        )
    }
}