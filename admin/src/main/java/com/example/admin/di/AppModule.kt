package com.example.admin.di

import com.example.admin.service.FirebaseOperationService
import com.example.admin.viewmodel.AddBookViewModel
import com.example.admin.viewmodel.DeleteBookViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Muhamed Amin Hassan on 14,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

val appModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseOperationService(get()) }
    viewModel { AddBookViewModel(get()) }
    viewModel { DeleteBookViewModel(get()) }
}