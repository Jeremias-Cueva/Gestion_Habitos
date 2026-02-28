package com.example.gestionhabitos.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionhabitos.model.entitis.Categoria

@Dao // Indispensable para que AppDatabase lo reconozca
interface CategoriaDao {
    @Insert
    suspend fun insertar(categoria: Categoria)

    @Query("SELECT * FROM categorias")
    suspend fun obtenerTodas(): List<Categoria>
}