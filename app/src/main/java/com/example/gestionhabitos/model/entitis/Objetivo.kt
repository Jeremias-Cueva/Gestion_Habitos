package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "objetivos")
data class Objetivo(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose(serialize = false, deserialize = true) // No envía ID al crear
    var id: Int = 0,

    @SerializedName("titulo") @Expose var titulo: String = "",
    @SerializedName("descripcion") @Expose var descripcion: String? = null,
    @SerializedName("metaValor") @Expose var metaValor: Double = 0.0,
    @SerializedName("valorActual") @Expose var valorActual: Double = 0.0,
    @SerializedName("completado") @Expose var completado: Boolean = false
)