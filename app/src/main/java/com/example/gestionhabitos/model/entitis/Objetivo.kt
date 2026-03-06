package com.example.gestionhabitos.model.entitis // 👈 CORREGIDO: Debe ir en entitis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "objetivos")
data class Objetivo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String = "",
    val metaValor: Double,
    val valorActual: Double = 0.0,
    val completado: Boolean = false,
    val usuarioEmail: String,
    val sincronizado: Boolean = false
)