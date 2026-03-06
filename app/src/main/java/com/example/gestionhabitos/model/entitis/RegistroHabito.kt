package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "registro_habitos",
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
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose(serialize = false, deserialize = true)
    var id: Int = 0,

    @SerializedName("habitoid") @Expose var habitoId: Int = 0, // Ojo: minúsculas si así está en SQL
    @SerializedName("fecha") @Expose var fecha: String = "",
    @SerializedName("completado") @Expose var completado: Boolean = false
)// ¡ASEGÚRATE DE QUE NO HAYA NADA MÁS AQUÍ ABAJO!