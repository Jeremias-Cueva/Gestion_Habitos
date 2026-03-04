package com.example.gestionhabitos.model.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val ZEN_URL = "https://zenquotes.io/api/"
    private const val MOCK_API_URL = "https://69a84ac637caab4b8c615068.mockapi.io/"

    // Cliente para evitar el error "Connection timed out"
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val frases: FraseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ZEN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FraseApiService::class.java)
    }

    val habitFlow: HabitFlowApiService by lazy {
        Retrofit.Builder()
            .baseUrl(MOCK_API_URL)
            .client(okHttpClient) // Aplicamos el cliente con más tiempo
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HabitFlowApiService::class.java)
    }
}