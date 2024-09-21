package com.example.readmate.di

import org.koin.core.module.Module

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

val appModule = listOf(
    firebaseModule,
    repoModule,
    viewModelModule
)