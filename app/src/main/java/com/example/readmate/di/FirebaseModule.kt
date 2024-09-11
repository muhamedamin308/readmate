package com.example.readmate.di

import android.app.Application
import android.content.SharedPreferences
import com.example.readmate.data.service.firebase.FirebaseAuthService
import com.example.readmate.ui.introduction.viewmodel.OnBoardingViewModel
import com.example.readmate.util.Constants
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Muhamed Amin Hassan on 10,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single<SharedPreferences> {
        val application: Application = get()
        application.getSharedPreferences(
            Constants.sharedPreferencesName,
            Application.MODE_PRIVATE
        )
    }
    single { FirebaseAuthService(get()) }
    viewModel { OnBoardingViewModel(get(), get()) }
}