package com.example.readmate.di

import com.example.readmate.ui.auth.viewmodel.AuthViewModel
import com.example.readmate.ui.onboarding.viewmodel.OnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

val viewModelModule = module {
    viewModel { OnBoardingViewModel(userRepository = get(), preferences = get()) }
    viewModel { AuthViewModel(userRepository = get()) }
}