package com.example.gestionhabitos.model.api

import com.google.gson.annotations.SerializedName

data class FraseResponse(
    @SerializedName("q") val texto: String,
    @SerializedName("a") val autor: String
) // <--- Asegúrate de tener este paréntesis de cierre