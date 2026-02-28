package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Habito
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val habitoDao = AppDatabase.getDatabase(application).habitoDao()

    // Observamos la lista de hábitos desde Room
    val listaHabitos: LiveData<List<Habito>> = habitoDao.obtenerTodosLosHabitos().asLiveData()

    // Transformación: Calculamos el porcentaje cada vez que la lista cambia
    val porcentajeProgreso: LiveData<Int> = listaHabitos.map { habitos ->
        if (habitos.isEmpty()) 0
        else {
            val completados = habitos.count { it.completado }
            (completados * 100) / habitos.size
        }
    }

    // LiveData para la frase del día
    private val _fraseMotivacional = MutableLiveData<String>()
    val fraseMotivacional: LiveData<String> get() = _fraseMotivacional

    init {
        generarFraseAleatoria()
    }

    private fun generarFraseAleatoria() {
        val frases = listOf(
            "La disciplina es el puente entre las metas y los logros.",
            "Tus hábitos determinan tu futuro.",
            "Pequeños pasos, grandes resultados.",
            "Hazlo hoy, no mañana.",
            "La constancia vence a la inteligencia."
        )
        _fraseMotivacional.value = frases.random()
    }
}