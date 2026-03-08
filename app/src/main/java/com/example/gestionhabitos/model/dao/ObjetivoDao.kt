package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.Objetivo
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjetivoDao {
    @Query("SELECT * FROM objetivos WHERE usuarioEmail = :email ORDER BY id DESC")
    fun obtenerObjetivosPorUsuario(email: String): Flow<List<Objetivo>>

    @Query("SELECT * FROM objetivos WHERE usuarioEmail = :email AND sincronizado = 0")
    suspend fun obtenerObjetivosPendientes(email: String): List<Objetivo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(objetivo: Objetivo): Long

    @Update
    suspend fun actualizar(objetivo: Objetivo)

    @Delete
    suspend fun eliminar(objetivo: Objetivo)

    @Transaction
    suspend fun guardarListaDeNube(objetivos: List<Objetivo>) {
        objetivos.forEach { objetivo ->
            insertar(objetivo.copy(sincronizado = true))
        }
    }

    // 🔥 REINICIO DIARIO: Poner a 0 el valor actual de los objetivos de un usuario
    @Query("UPDATE objetivos SET valorActual = 0.0, completado = 0 WHERE usuarioEmail = :email")
    suspend fun reiniciarObjetivosDiarios(email: String)
}