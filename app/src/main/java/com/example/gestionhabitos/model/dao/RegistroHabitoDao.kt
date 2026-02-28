package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.RegistroHabito
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroHabitoDao {
    // Cambiado de registros_habitos_table a registro_habitos
    @Query("SELECT * FROM registro_habitos WHERE habitoId = :habitoId")
    fun obtenerRegistrosPorHabito(habitoId: Int): Flow<List<RegistroHabito>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(registro: RegistroHabito)
}