package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "registro_habitos", // Esto resuelve el error "Cannot resolve symbol registro_habitos"
    foreignKeys = [
        ForeignKey(
            entity = Habito::class,
            parentColumns = ["id"],
            childColumns = ["habitoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RegistroHabito(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitoId: Int, // Esto resuelve el error "Cannot resolve symbol habitoId"
    val fecha: String, // Esto resuelve el error "Cannot resolve symbol fecha"
    val completado: Boolean
)