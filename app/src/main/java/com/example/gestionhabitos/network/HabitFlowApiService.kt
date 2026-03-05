package com.example.gestionhabitos.network

import com.example.gestionhabitos.model.entitis.Usuario
import retrofit2.Response
import retrofit2.http.*

interface HabitFlowApiService {

    @GET("usuarios")
    suspend fun buscarUsuarioPorEmail(
        @Query("email") email: String
    ): Response<List<Usuario>>

    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    // Nuevo método para actualizar la contraseña (o cualquier dato) del usuario en MockAPI
    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: String,
        @Body usuario: Usuario
    ): Response<Usuario>
}