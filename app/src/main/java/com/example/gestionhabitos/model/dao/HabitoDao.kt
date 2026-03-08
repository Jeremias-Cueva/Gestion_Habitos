package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.Habito
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitoDao {

    @Query("SELECT * FROM habitos WHERE usuarioEmail = :email ORDER BY nombre ASC")
    fun obtenerHabitosPorUsuario(email: String): Flow<List<Habito>>

    @Query("SELECT * FROM habitos WHERE usuarioEmail = :email AND sincronizado = 0")
    suspend fun obtenerHabitosPendientes(email: String): List<Habito>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(habito: Habito): Long

    @Update
    suspend fun actualizar(habito: Habito)

    @Delete
    suspend fun eliminar(habito: Habito)

    @Transaction
    suspend fun insertarOActualizar(habito: Habito) {
        val id = insertar(habito)
        if (id == -1L) {
            actualizar(habito)
        }
    }

    // 🔥 REINICIO DIARIO: Desmarcar todos los hábitos de un usuario
    @Query("UPDATE habitos SET completado = 0 WHERE usuarioEmail = :email")
    suspend fun reiniciarHabitosDiarios(email: String)
}