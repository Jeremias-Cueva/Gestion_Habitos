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

    // ✅ ACTUALIZADO: Cambiamos {id} por {email} para que coincida con tu lógica
    @PUT("usuarios/{email}")
    suspend fun actualizarUsuario(
        @Path("email") email: String, // 🚩 Ahora recibe el correo como identificador
        @Body usuario: Usuario
    ): Response<Usuario>
}