package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.Habito
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitoDao {

    // Usamos LOWER para asegurar que el filtro sea exacto sin importar mayúsculas
    @Query("SELECT * FROM habitos WHERE LOWER(usuarioEmail) = LOWER(:email) ORDER BY id ASC")
    fun obtenerHabitosPorUsuario(email: String): Flow<List<Habito>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(habito: Habito)

    @Update
    suspend fun actualizar(habito: Habito)

    @Delete
    suspend fun eliminar(habito: Habito)
}