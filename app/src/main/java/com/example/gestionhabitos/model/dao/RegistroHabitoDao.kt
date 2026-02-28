package com.example.gestionhabitos.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionhabitos.model.entitis.RegistroHabito

@Dao // Indispensable para que Room lo reconozca
interface RegistroHabitoDao { // <-- Verifica que este nombre coincida exactamente

    @Insert
    suspend fun insertar(registro: RegistroHabito)

    @Query("SELECT * FROM registro_habitos WHERE habitoId = :habitoId")
    suspend fun obtenerPorHabito(habitoId: Int): List<RegistroHabito>
}