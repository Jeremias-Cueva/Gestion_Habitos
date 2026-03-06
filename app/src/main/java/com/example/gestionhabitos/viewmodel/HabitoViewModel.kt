package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Habito
import com.example.gestionhabitos.model.repository.HabitoRepository
import kotlinx.coroutines.launch

class HabitoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository: HabitoRepository = HabitoRepository(
        database.habitoDao(),
        database.categoriaDao(),
        database.registroHabitoDao()
    )

    private val userEmail = MutableLiveData<String>()

    // Observa Room (Local) y se actualiza en tiempo real en la UI
    val listaHabitos: LiveData<List<Habito>> = userEmail.switchMap { email ->
        repository.obtenerHabitosDeUsuario(email).asLiveData()
    }

    val porcentajeProgreso: LiveData<Int> = listaHabitos.map { habitos ->
        if (habitos.isNullOrEmpty()) 0
        else {
            val completados = habitos.count { it.completado }
            (completados * 100) / habitos.size
        }
    }

    // CARGA INTELIGENTE: Dispara la observación local y la sincronización con la nube
    fun cargarHabitosDeUsuario(email: String?) {
        if (email.isNullOrEmpty()) return

        userEmail.value = email

        // Lanzamos la sincronización en segundo plano
        viewModelScope.launch {
            // 1. Bajamos lo que hay en Supabase
            repository.descargarHabitosDeNube(email)
            // 2. Subimos lo que se quedó offline en el celular
            repository.sincronizarHabitosPendientes(email)
        }
    }

    fun insertar(habito: Habito) = viewModelScope.launch {
        repository.insertarHabito(habito)
    }

    fun actualizarHabito(habito: Habito) = viewModelScope.launch {
        repository.actualizarHabito(habito)
    }

    fun actualizarEstadoHabito(habito: Habito, isChecked: Boolean) {
        viewModelScope.launch {
            val habitoActualizado = habito.copy(completado = isChecked)
            repository.actualizarHabito(habitoActualizado)
            repository.registrarActividad(habito.id, isChecked)
        }
    }

    fun eliminar(habito: Habito) = viewModelScope.launch {
        repository.eliminarHabito(habito)
    }
}