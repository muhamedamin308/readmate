package com.example.readmate.di

import com.example.readmate.ui.auth.viewmodel.AuthViewModel
import com.example.readmate.ui.explore.viewmodel.ExploreViewModel
import com.example.readmate.ui.library.viewmodel.FirebaseBookDetailViewModel
import com.example.readmate.ui.library.viewmodel.LibraryViewModel
import com.example.readmate.ui.onboarding.viewmodel.OnBoardingViewModel
import com.example.readmate.ui.payment.viewmodel.PaymentViewModel
import com.example.readmate.ui.settings.viewmodel.BookcaseViewModel
import com.example.readmate.ui.settings.viewmodel.EditProfileViewModel
import com.example.readmate.ui.settings.viewmodel.NotificationsViewModel
import com.example.readmate.ui.settings.viewmodel.SettingsViewModel
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
    viewModel { LibraryViewModel(bookRepository = get()) }
    viewModel { ExploreViewModel(apiBookRepository = get()) }
    viewModel { SettingsViewModel(userRepository = get()) }
    viewModel { EditProfileViewModel(userRepository = get()) }
    viewModel { NotificationsViewModel(userServicesRepository = get()) }
    viewModel { PaymentViewModel(userServicesRepository = get()) }
    viewModel {
        FirebaseBookDetailViewModel(
            userServicesRepository = get(),
            bookServicesRepository = get()
        )
    }
    viewModel { BookcaseViewModel(userServicesRepository = get()) }
}