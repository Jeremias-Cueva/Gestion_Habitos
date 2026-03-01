package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    // Esta es la línea que falta
    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun obtenerUsuarioPorId(id: Int): Flow<Usuario>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)

    @Update
    suspend fun actualizar(usuario: Usuario)
}