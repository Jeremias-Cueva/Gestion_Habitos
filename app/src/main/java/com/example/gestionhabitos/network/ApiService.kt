package com.example.gestionhabitos.model.api

import retrofit2.http.GET

interface FraseApiService {
    @GET("random") // Obtenemos una frase aleatoria
    suspend fun obtenerFraseAleatoria(): List<FraseResponse>
}