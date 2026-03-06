package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Objetivo
import com.example.gestionhabitos.model.repository.ObjetivoRepository
import kotlinx.coroutines.launch

class ObjetivoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ObjetivoRepository

    init {
        val objetivoDao = AppDatabase.getDatabase(application).objetivoDao()
        repository = ObjetivoRepository(objetivoDao)
    }

    // 🛡️ SOLUCIÓN: Agregamos el método que faltaba
    fun obtenerObjetivos(email: String): LiveData<List<Objetivo>> {
        return repository.obtenerObjetivosDeUsuario(email).asLiveData()
    }

    // 🛡️ SOLUCIÓN: Agregamos la función para eliminar
    fun eliminarObjetivo(objetivo: Objetivo) = viewModelScope.launch {
        repository.eliminarObjetivo(objetivo)
    }

    fun insertarObjetivo(objetivo: Objetivo) = viewModelScope.launch {
        repository.insertarObjetivo(objetivo)
    }
}