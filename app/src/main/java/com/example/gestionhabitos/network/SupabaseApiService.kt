package com.example.gestionhabitos.network

import com.example.gestionhabitos.model.entitis.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApiService {

    // 1. Busca el usuario para el Login o validación (El GET que agregamos recién)
    @GET("usuarios?select=*")
    suspend fun buscarUsuarioPorEmail(@Query("email") emailEq: String): Response<List<Usuario>>

    // 2. Envía un usuario nuevo a la tabla "usuarios" en Supabase (El que se había borrado)
    @POST("usuarios")
    suspend fun sincronizarUsuario(@Body usuario: Usuario): Response<Void>
}