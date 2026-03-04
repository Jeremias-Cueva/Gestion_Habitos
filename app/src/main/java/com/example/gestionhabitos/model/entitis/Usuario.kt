package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String = "",
    val email: String,      // Cambiado de 'correo' a 'email'
    val password: String,   // Cambiado de 'contrasena' a 'password'
    val fotoUri: String? = null
)