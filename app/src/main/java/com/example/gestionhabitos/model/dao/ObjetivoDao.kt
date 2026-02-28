package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.Objetivo
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjetivoDao {

    @Query("SELECT * FROM objetivos ORDER BY id ASC")
    fun obtenerTodosLosObjetivos(): Flow<List<Objetivo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(objetivo: Objetivo)

    @Update
    suspend fun actualizar(objetivo: Objetivo)

    @Delete
    suspend fun eliminar(objetivo: Objetivo)

    @Query("SELECT * FROM objetivos WHERE id = :id")
    suspend fun obtenerObjetivoPorId(id: Int): Objetivo?
}