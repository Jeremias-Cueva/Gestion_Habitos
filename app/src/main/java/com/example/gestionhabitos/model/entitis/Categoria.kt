package com.example.gestionhabitos.model.entitis

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose(serialize = false, deserialize = true)
    var id: Int = 0,

    @SerializedName("nombre") @Expose var nombre: String = "",
    @SerializedName("descripcion") @Expose var descripcion: String? = null
)