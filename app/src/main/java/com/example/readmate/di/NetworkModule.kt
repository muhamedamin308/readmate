package com.example.readmate.di

import com.example.readmate.data.service.remote.api.BookApiService
import com.example.readmate.data.service.remote.api.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
val networkModule = module {
    single { NetworkConnectionInterceptor(androidContext()) }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<NetworkConnectionInterceptor>())
            .build()
    }
    single {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl("https://www.dbooks.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single {
        get<Retrofit>().create(BookApiService::class.java)
    }
}