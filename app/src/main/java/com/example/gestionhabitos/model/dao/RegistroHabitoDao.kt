package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.RegistroHabito
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroHabitoDao {
    @Query("SELECT * FROM registro_habitos WHERE habitoId = :habitoId")
    fun obtenerRegistrosPorHabito(habitoId: Int): Flow<List<RegistroHabito>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(registro: RegistroHabito)

    // Consulta para obtener la cantidad de hábitos completados por fecha (últimos 7 días)
    @Query("SELECT fecha, COUNT(*) as cantidad FROM registro_habitos WHERE completado = 1 GROUP BY fecha ORDER BY fecha ASC LIMIT 7")
    fun obtenerProgresoSemanal(): Flow<List<ProgresoDiario>>
}

data class ProgresoDiario(
    val fecha: String,
    val cantidad: Int
)