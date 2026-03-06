package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    // 🚩 Esta es la función que le falta al ViewModel
    @Query("SELECT * FROM usuarios LIMIT 1")
    fun obtenerSesionActiva(): kotlinx.coroutines.flow.Flow<Usuario?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)

    @Update
    suspend fun actualizar(usuario: Usuario)

    // Para el logout, borramos todo lo que haya en la tabla local
    @Query("DELETE FROM usuarios")
    suspend fun borrarSesion()
}