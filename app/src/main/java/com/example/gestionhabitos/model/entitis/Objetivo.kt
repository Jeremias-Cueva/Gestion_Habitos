package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "objetivos") // Esta anotación soluciona el error de KSP
data class Objetivo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String? = null,
    val metaValor: Double,
    val valorActual: Double = 0.0,
    val completado: Boolean = false
)