package com.example.gestionhabitos.model.dao

import androidx.room.*
import com.example.gestionhabitos.model.entitis.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    // Cambiado de categorias_table a categorias para coincidir con tu Entity
    @Query("SELECT * FROM categorias")
    fun obtenerCategorias(): Flow<List<Categoria>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(categoria: Categoria)

    @Delete
    suspend fun eliminar(categoria: Categoria)
}