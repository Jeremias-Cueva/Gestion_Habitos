package com.example.gestionhabitos.model.api

import retrofit2.http.GET

interface FraseApiService {
    @GET("random") // Esto completa la URL: https://zenquotes.io/api/random
    suspend fun obtenerFraseAleatoria(): List<FraseResponse>
}