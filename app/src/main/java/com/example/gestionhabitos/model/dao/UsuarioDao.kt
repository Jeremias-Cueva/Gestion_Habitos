package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.Usuario
import kotlinx.coroutines.flow.Flow // El import debe estar aquí

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios LIMIT 1")
    fun obtenerSesionActiva(): Flow<Usuario?> // Aquí solo Flow

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)

    @Update
    suspend fun actualizar(usuario: Usuario)

    @Query("DELETE FROM usuarios")
    suspend fun borrarSesion()
}