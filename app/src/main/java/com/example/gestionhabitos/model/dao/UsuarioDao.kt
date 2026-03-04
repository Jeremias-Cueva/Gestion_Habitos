package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun obtenerUsuarioPorId(id: Int): Flow<Usuario?>

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerUsuarioPorIdDirecto(id: Int): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)

    @Update
    suspend fun actualizar(usuario: Usuario)

    // Esta es la función que corregirá el error "Unresolved reference: borrarPorId"
    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun borrarPorId(id: Int)

    @Delete
    suspend fun eliminar(usuario: Usuario)
}