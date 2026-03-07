package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "objetivos")
data class Objetivo(
    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false, deserialize = true) // El ID lo genera la base de datos
    val id: Int = 0,

    @Expose
    @SerializedName("titulo")
    val titulo: String,

    @Expose
    @SerializedName("descripcion")
    val descripcion: String = "",

    @Expose
    @SerializedName("metaValor")
    val metaValor: Double,

    @Expose
    @SerializedName("valorActual")
    val valorActual: Double = 0.0,

    @Expose
    @SerializedName("completado")
    val completado: Boolean = false,

    @Expose
    @SerializedName("usuarioEmail")
    val usuarioEmail: String,

    // Este campo es solo para control interno de Room, no va a la nube
    val sincronizado: Boolean = false
)