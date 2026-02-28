package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Habito

class EstadisticasViewModel(application: Application) : AndroidViewModel(application) {
    private val habitoDao = AppDatabase.getDatabase(application).habitoDao()

    // Observamos todos los hábitos
    val listaHabitos: LiveData<List<Habito>> = habitoDao.obtenerTodosLosHabitos().asLiveData()

    // Calculamos el progreso total para el gráfico circular
    val estadisticasProgreso: LiveData<Pair<Int, Int>> = listaHabitos.map { habitos ->
        val total = habitos.size
        val completados = habitos.count { it.completado }
        Pair(completados, total)
    }
}