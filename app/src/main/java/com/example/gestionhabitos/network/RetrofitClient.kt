package com.example.gestionhabitos.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val ZEN_URL = "https://zenquotes.io/api/"

    // URL DE MOCKAPI (Reemplaza a la IP local y al servidor de Node)
    // Se añade un "/" al final para que Retrofit funcione correctamente
    private const val MOCK_API_URL = "https://69a84ac637caab4b8c615068.mockapi.io/"

    // Instancia para las frases motivacionales
    val frases: FraseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ZEN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FraseApiService::class.java)
    }

    // Instancia para HabitFlow (MockAPI)
    val habitFlow: HabitFlowApiService by lazy {
        Retrofit.Builder()
            .baseUrl(MOCK_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HabitFlowApiService::class.java)
    }
}