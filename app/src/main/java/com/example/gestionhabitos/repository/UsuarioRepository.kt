package com.example.gestionhabitos.model.repository

import com.example.gestionhabitos.model.api.RetrofitClient
import com.example.gestionhabitos.model.entitis.Usuario
import retrofit2.Response

class UsuarioRepository {
    suspend fun registrarEnNube(usuario: Usuario): Response<Usuario> {
        return RetrofitClient.habitFlow.registrarUsuario(usuario)
    }
}