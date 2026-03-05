package com.example.gestionhabitos.network

import com.example.gestionhabitos.model.entitis.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query

interface HabitFlowApiService {

    @GET("usuarios")
    suspend fun buscarUsuarioPorEmail(
        @Query("email") email: String
    ): Response<List<Usuario>>

    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>
}