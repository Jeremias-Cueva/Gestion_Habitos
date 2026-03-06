package com.example.gestionhabitos.network

import com.example.gestionhabitos.model.entitis.*
import retrofit2.Response
import retrofit2.http.*

interface SupabaseApiService {

    @GET("usuarios?select=*")
    suspend fun buscarUsuarioPorEmail(@Query("email") emailEq: String): Response<List<Usuario>>

    @POST("usuarios")
    suspend fun sincronizarUsuario(@Body usuario: Usuario): Response<Void>

    // --- HABITOS ---
    @GET("habitos?select=*")
    suspend fun obtenerHabitos(@Query("usuarioEmail") emailEq: String): Response<List<Habito>>

    @POST("habitos")
    suspend fun insertarHabito(@Body habito: Habito): Response<Void>

    // --- REGISTRO HABITOS ---
    @POST("registro_habitos")
    suspend fun insertarRegistro(@Body registro: RegistroHabito): Response<Void>
}