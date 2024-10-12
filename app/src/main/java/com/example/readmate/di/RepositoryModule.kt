package com.example.readmate.di

import com.example.readmate.data.repo.remote.api.ApiBookRepository
import com.example.readmate.data.repo.remote.api.ApiBookRepositoryImpl
import com.example.readmate.data.repo.remote.firebase.auth.FirebaseUserRepository
import com.example.readmate.data.repo.remote.firebase.auth.FirebaseUserRepositoryImpl
import com.example.readmate.data.repo.remote.firebase.book.FirebaseBookRepository
import com.example.readmate.data.repo.remote.firebase.book.FirebaseBookRepositoryImpl
import com.example.readmate.data.repo.remote.firebase.user.UserServicesRepository
import com.example.readmate.data.repo.remote.firebase.user.UserServicesRepositoryImpl
import org.koin.dsl.module

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

val repoModule = module {
    single<FirebaseUserRepository> {
        FirebaseUserRepositoryImpl(
            authService = get(),
            userService = get()
        )
    }
    single<FirebaseBookRepository> { FirebaseBookRepositoryImpl(bookService = get()) }
    single<ApiBookRepository> { ApiBookRepositoryImpl(apiService = get()) }
    single<UserServicesRepository> {
        UserServicesRepositoryImpl(
            userService = get(),
            paymentService = get()
        )
    }
}