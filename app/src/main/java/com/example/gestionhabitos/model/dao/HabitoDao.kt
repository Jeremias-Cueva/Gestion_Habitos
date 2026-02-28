package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.Habito
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitoDao {

    @Query("SELECT * FROM habitos ORDER BY id ASC")
    fun obtenerTodosLosHabitos(): Flow<List<Habito>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(habito: Habito)

    @Update
    suspend fun actualizar(habito: Habito)

    @Delete
    suspend fun eliminar(habito: Habito)
}