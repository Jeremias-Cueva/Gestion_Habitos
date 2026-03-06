package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "registro_habitos")
data class RegistroHabito(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose(serialize = false, deserialize = true)
    var id: Int = 0,

    @SerializedName("habitoId") // 🚩 CORREGIDO: En tu foto sale así con I mayúscula
    @Expose
    var habitoId: Int,

    @SerializedName("fecha")
    @Expose
    var fecha: String,

    @SerializedName("completado")
    @Expose
    var completado: Boolean
)// ¡ASEGÚRATE DE QUE NO HAYA NADA MÁS AQUÍ ABAJO!