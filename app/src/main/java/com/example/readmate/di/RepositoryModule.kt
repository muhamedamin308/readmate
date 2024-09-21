package com.example.readmate.di

import com.example.readmate.data.repo.remote.firebase.FirebaseUserRepository
import com.example.readmate.data.repo.remote.firebase.FirebaseUserRepositoryImpl
import org.koin.dsl.module

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

val repoModule = module {
    single<FirebaseUserRepository> { FirebaseUserRepositoryImpl(authService = get()) }
}