package com.example.gestionhabitos.model.api

import com.example.gestionhabitos.model.entitis.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query

interface HabitFlowApiService {

    // En MockAPI, para "loguear", lo estándar es buscar en la lista de usuarios
    @GET("usuarios")
    suspend fun buscarUsuarioPorEmail(
        @Query("email") email: String
    ): Response<List<Usuario>>

    // Para el registro, MockAPI acepta un POST al recurso y devuelve el objeto creado
    // En tu interfaz de Retrofit agrega:
    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>
}
