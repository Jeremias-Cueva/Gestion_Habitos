package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import kotlinx.coroutines.flow.map

class EstadisticasViewModel(application: Application) : AndroidViewModel(application) {
    private val habitoDao = AppDatabase.getDatabase(application).habitoDao()

    // Resumen de estadísticas para la vista de Progreso
    val totalHabitos: LiveData<Int> = habitoDao.obtenerTodosLosHabitos().asLiveData().map { it.size }
    val habitosCompletados: LiveData<Int> = habitoDao.obtenerTodosLosHabitos().asLiveData().map { lista ->
        lista.count { it.completado }
    }
}