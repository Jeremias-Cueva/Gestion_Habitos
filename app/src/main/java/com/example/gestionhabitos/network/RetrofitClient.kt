package com.example.gestionhabitos.model.api

import retrofit2.Retrofit
// Cambia la que tienes por esta exacta:
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://zenquotes.io/api/"

    val instance: FraseApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Aquí debe decir GsonConverterFactory
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(FraseApiService::class.java)
    }
}