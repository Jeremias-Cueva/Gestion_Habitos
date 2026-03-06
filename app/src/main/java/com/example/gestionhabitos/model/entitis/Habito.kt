package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "habitos")
data class Habito(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose(serialize = false, deserialize = true)
    var id: Int = 0,

    @SerializedName("nombre")
    @Expose
    var nombre: String = "",

    @SerializedName("categoria")
    @Expose
    var categoria: String = "",

    @SerializedName("hora")
    @Expose
    var hora: String = "",

    @SerializedName("completado")
    @Expose
    var completado: Boolean = false,

    @SerializedName("usuarioEmail")
    @Expose
    var usuarioEmail: String = "",

    @Ignore
    @Expose(serialize = false, deserialize = false)
    var sincronizado: Boolean = false
)