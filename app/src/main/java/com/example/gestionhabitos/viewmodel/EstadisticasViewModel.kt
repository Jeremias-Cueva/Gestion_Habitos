package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.dao.ProgresoDiario
import com.example.gestionhabitos.model.entitis.Habito

class EstadisticasViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val habitoDao = db.habitoDao()
    private val registroDao = db.registroHabitoDao()

    // 1. Datos para el gráfico circular (Hoy)
    val listaHabitos: LiveData<List<Habito>> = habitoDao.obtenerTodosLosHabitos().asLiveData()
    val estadisticasProgreso: LiveData<Pair<Int, Int>> = listaHabitos.map { habitos ->
        val total = habitos.size
        val completados = habitos.count { it.completado }
        Pair(completados, total)
    }

    // 2. Datos para el gráfico de barras (Semanal)
    // Se actualizará automáticamente cada vez que se inserte un RegistroHabito
    val progresoSemanal: LiveData<List<ProgresoDiario>> = registroDao.obtenerProgresoSemanal().asLiveData()
}