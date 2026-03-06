package com.example.gestionhabitos.model.entitis

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    @SerializedName("email")
    @Expose
    var email: String = "",

    @SerializedName("password")
    @Expose
    var password: String = "",

    @SerializedName("nombre")
    @Expose
    var nombre: String = ""
)