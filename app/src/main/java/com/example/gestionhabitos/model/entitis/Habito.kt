package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitos")
data class Habito(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val categoria: String,
    val hora: String = "", // Nuevo campo para la hora (ej: "14:30")
    val completado: Boolean = false
)