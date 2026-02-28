package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitos")
data class Habito(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,    // Asegúrate de que se llame exactamente 'nombre'
    val categoria: String, // Asegúrate de que se llame exactamente 'categoria'
    val completado: Boolean = false
)