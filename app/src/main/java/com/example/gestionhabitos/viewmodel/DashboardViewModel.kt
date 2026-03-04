package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Habito
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val habitoDao = AppDatabase.getDatabase(application).habitoDao()

    // 1. LiveData para almacenar el email del usuario activo
    private val userEmail = MutableLiveData<String>()

    // 2. Usamos switchMap para llamar a obtenerHabitosPorUsuario (la función que sí existe en tu DAO)
    val listaHabitos: LiveData<List<Habito>> = userEmail.switchMap { email ->
        habitoDao.obtenerHabitosPorUsuario(email).asLiveData()
    }

    // 3. Calculamos el porcentaje basado en la lista filtrada
    val porcentajeProgreso: LiveData<Int> = listaHabitos.map { habitos ->
        if (habitos.isNullOrEmpty()) 0
        else {
            val completados = habitos.count { it.completado }
            (completados * 100) / habitos.size
        }
    }

    // Método para cargar los datos del usuario desde el Fragment
    fun cargarDatosUsuario(email: String) {
        userEmail.value = email
    }

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