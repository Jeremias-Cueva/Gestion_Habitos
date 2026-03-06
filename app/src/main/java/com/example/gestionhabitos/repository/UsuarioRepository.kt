package com.example.gestionhabitos.repository

import com.example.gestionhabitos.model.entitis.Usuario
import com.example.gestionhabitos.network.SupabaseClient
import retrofit2.Response

class UsuarioRepository {

    // 1. Busca el usuario para el Login o validación
    suspend fun buscarUsuarioPorEmail(email: String): Response<List<Usuario>> {
        // Limpiamos espacios y pasamos a minúsculas para que coincida con la DB
        val emailLimpio = email.trim().lowercase()
        return SupabaseClient.apiService.buscarUsuarioPorEmail("eq.$emailLimpio")
    }

    // 2. Registra el usuario nuevo
    suspend fun registrarEnNube(usuario: Usuario): Response<Void> {
        return SupabaseClient.apiService.sincronizarUsuario(usuario)
    }
}