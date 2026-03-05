package com.example.gestionhabitos.repository

import com.example.gestionhabitos.model.entitis.Usuario
import com.example.gestionhabitos.network.RetrofitClient
import retrofit2.Response

class UsuarioRepository {
    suspend fun registrarEnNube(usuario: Usuario): Response<Usuario> {
        return RetrofitClient.habitFlow.registrarUsuario(usuario)
    }
}