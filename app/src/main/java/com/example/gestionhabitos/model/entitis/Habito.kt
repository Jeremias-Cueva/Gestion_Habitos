package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey
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

    @SerializedName("categoria_id")
    @Expose
    var categoriaId: Int? = null,

    @SerializedName("hora")
    @Expose
    var hora: String = "",

    @SerializedName("completado")
    @Expose
    var completado: Boolean = false,

    @SerializedName("usuario_email")
    @Expose
    var usuarioEmail: String = "",

    @Expose(serialize = false, deserialize = false)
    var sincronizado: Boolean = false
) // <--- TERMINA EN PARÉNTESIS. Sin llaves abajo.