package com.example.gestionhabitos.network

import com.example.gestionhabitos.model.api.FraseApiService // 🚩 Importación explícita
import com.example.gestionhabitos.model.api.SendGridApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val ZEN_URL = "https://zenquotes.io/api/"
    private const val MOCK_API_URL = "https://69a84ac637caab4b8c615068.mockapi.io/"
    private const val SENDGRID_URL = "https://api.sendgrid.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // 🚩 Corregido: Uso de la interfaz con import simple
    val frases: FraseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ZEN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FraseApiService::class.java)
    }

    // Nota: Si HabitFlowApiService te da error, asegúrate de que también esté importado
    val habitFlow: HabitFlowApiService by lazy {
        Retrofit.Builder()
            .baseUrl(MOCK_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HabitFlowApiService::class.java)
    }

    val sendGrid: SendGridApiService by lazy {
        Retrofit.Builder()
            .baseUrl(SENDGRID_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SendGridApiService::class.java)
    }
}