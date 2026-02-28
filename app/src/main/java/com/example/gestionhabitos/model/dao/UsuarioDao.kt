package com.example.gestionhabitos.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionhabitos.model.entitis.Usuario

@Dao // Indispensable
interface UsuarioDao {
    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios LIMIT 1")
    suspend fun obtenerUsuario(): Usuario?
}