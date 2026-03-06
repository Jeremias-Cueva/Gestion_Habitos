package com.example.gestionhabitos.network

import com.example.gestionhabitos.model.entitis.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ---------- USUARIOS ----------
    @GET("usuarios")
    suspend fun buscarUsuarioPorEmail(@Query("email") filtro: String): Response<List<Usuario>>

    @POST("usuarios")
    suspend fun sincronizarUsuario(@Body usuario: Usuario): Response<Void>

    // ---------- HABITOS ----------
    @GET("habitos")
    suspend fun obtenerHabitos(@Query("usuario_email") filtro: String): Response<List<Habito>>

    @Headers("Prefer: return=representation")
    @POST("habitos")
    suspend fun insertarHabito(@Body habito: Habito): Response<List<Habito>>

    @PATCH("habitos")
    suspend fun actualizarHabitoEnNube(@Query("id") filtro: String, @Body habito: Habito): Response<Unit>

    @DELETE("habitos")
    suspend fun eliminarHabitoEnNube(@Query("id") filtro: String): Response<Unit>

    // ---------- REGISTRO HABITOS ----------
    @Headers("Prefer: return=minimal")
    @POST("registro_habitos")
    suspend fun insertarRegistro(@Body registro: RegistroHabito): Response<Void>

    // ---------- OBJETIVOS (CORREGIDO Y COMPLETO) ----------

    @GET("objetivos")
    suspend fun obtenerObjetivos(
        @Query("usuario_email") filtro: String // Ejemplo: "eq.tu@email.com"
    ): Response<List<Objetivo>>

    @Headers("Prefer: return=representation") // Cambiado a representation para obtener el ID de la nube
    @POST("objetivos")
    suspend fun insertarObjetivoEnNube(
        @Body objetivo: Objetivo
    ): Response<List<Objetivo>>

    @PATCH("objetivos")
    suspend fun actualizarObjetivoEnNube(
        @Query("id") filtro: String,
        @Body objetivo: Objetivo
    ): Response<Unit>

    @DELETE("objetivos")
    suspend fun eliminarObjetivoEnNube(
        @Query("id") filtro: String
    ): Response<Unit>
}