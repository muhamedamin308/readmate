package com.example.readmate.di

import com.example.readmate.data.service.remote.api.BookApiService
import com.example.readmate.data.service.remote.api.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Muhamed Amin Hassan on 17,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
val networkModule = module {

    // Provide the NetworkConnectionInterceptor which checks internet connectivity
    single { NetworkConnectionInterceptor(androidContext()) }

    // Provide OkHttpClient with the NetworkConnectionInterceptor and optional logging for debugging
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<NetworkConnectionInterceptor>()) // Adding network interceptor
            .addInterceptor(HttpLoggingInterceptor().apply { // Adding logging interceptor for debugging
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Set write timeout
            .build()
    }

    // Provide Retrofit instance using the OkHttpClient
    single {
        Retrofit.Builder()
            .client(get<OkHttpClient>()) // Use the custom OkHttpClient
            .baseUrl("https://www.dbooks.org/api/") // Base URL for your API
            .addConverterFactory(GsonConverterFactory.create()) // Add Gson converter
            .build()
    }

    // Provide the BookApiService interface using Retrofit
    single {
        get<Retrofit>().create(BookApiService::class.java)
    }
}
