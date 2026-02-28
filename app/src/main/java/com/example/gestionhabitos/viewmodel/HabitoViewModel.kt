package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Habito
import com.example.gestionhabitos.model.repository.HabitoRepository
import kotlinx.coroutines.launch

class HabitoViewModel(application: Application) : AndroidViewModel(application) {

    // Obtenemos las instancias de los DAOs
    private val database = AppDatabase.getDatabase(application)
    private val repository: HabitoRepository

    val listaHabitos: LiveData<List<Habito>>
    val porcentajeProgreso: LiveData<Int>

    init {
        // Inicializamos el repositorio con todos los DAOs necesarios
        repository = HabitoRepository(
            database.habitoDao(),
            database.categoriaDao(),
            database.registroHabitoDao()
        )

        // Usamos el repositorio como única fuente de datos
        listaHabitos = repository.todosLosHabitos.asLiveData()

        porcentajeProgreso = listaHabitos.map { habitos ->
            if (habitos.isEmpty()) 0
            else {
                val completados = habitos.count { it.completado }
                (completados * 100) / habitos.size
            }
        }
    }

    fun insertar(habito: Habito) = viewModelScope.launch {
        repository.insertarHabito(habito)
    }

    // Esta es la función que usaremos para el Checkbox
    fun actualizarEstadoHabito(habito: Habito, isChecked: Boolean) {
        viewModelScope.launch {
            val habitoActualizado = habito.copy(completado = isChecked)
            repository.actualizarHabito(habitoActualizado)

            // Guardamos el registro histórico automáticamente
            repository.registrarActividad(habito.id, isChecked)
        }
    }

    fun eliminar(habito: Habito) = viewModelScope.launch {
        repository.eliminarHabito(habito)
    }
}