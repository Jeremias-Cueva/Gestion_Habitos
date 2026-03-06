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

    // Usamos IGNORE para que si el ID ya existe, no de error ni borre el actual
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(habito: Habito): Long

    @Update
    suspend fun actualizar(habito: Habito)

    @Delete
    suspend fun eliminar(habito: Habito)

    // 🔥 ESTA ES LA FUNCIÓN QUE TE DABA ERROR
    @Transaction
    suspend fun insertarOActualizar(habito: Habito) {
        val id = insertar(habito)
        if (id == -1L) {
            // Si el ID ya existe (retorna -1), entonces actualizamos el registro
            actualizar(habito)
        }
    }
}