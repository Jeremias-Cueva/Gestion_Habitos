package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Habito
import com.example.gestionhabitos.model.repository.HabitoRepository
import kotlinx.coroutines.launch

class HabitoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)

    // 1. Inicializamos el repositorio inmediatamente para que esté disponible para los LiveData
    private val repository: HabitoRepository = HabitoRepository(
        database.habitoDao(),
        database.categoriaDao(),
        database.registroHabitoDao()
    )

    // 2. LiveData que contendrá el email del usuario actual
    private val userEmail = MutableLiveData<String>()

    // 3. Transformamos el email en la lista de hábitos correspondiente
    // switchMap ahora encuentra al 'repository' ya inicializado
    val listaHabitos: LiveData<List<Habito>> = userEmail.switchMap { email ->
        repository.obtenerHabitosDeUsuario(email).asLiveData()
    }

    // 4. Cálculo del progreso basado en la lista filtrada
    val porcentajeProgreso: LiveData<Int> = listaHabitos.map { habitos ->
        if (habitos.isNullOrEmpty()) 0
        else {
            val completados = habitos.count { it.completado }
            (completados * 100) / habitos.size
        }
    }

    // Método que se llama desde el Fragment al detectar la sesión activa
    fun cargarHabitosDeUsuario(email: String) {
        userEmail.value = email
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