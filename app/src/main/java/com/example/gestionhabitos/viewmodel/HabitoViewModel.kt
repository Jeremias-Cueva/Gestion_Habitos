package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Habito
import kotlinx.coroutines.launch

class HabitoViewModel(application: Application) : AndroidViewModel(application) {
    private val habitoDao = AppDatabase.getDatabase(application).habitoDao()

    val listaHabitos: LiveData<List<Habito>> = habitoDao.obtenerTodosLosHabitos().asLiveData()

    // Nuevo: Calculamos el porcentaje de progreso automáticamente
    val porcentajeProgreso: LiveData<Int> = listaHabitos.map { habitos ->
        if (habitos.isEmpty()) 0
        else {
            val completados = habitos.count { it.completado }
            (completados * 100) / habitos.size
        }
    }

    fun insertar(habito: Habito) {
        viewModelScope.launch { habitoDao.insertar(habito) }
    }

    fun actualizar(habito: Habito) {
        viewModelScope.launch { habitoDao.actualizar(habito) }
    }
}