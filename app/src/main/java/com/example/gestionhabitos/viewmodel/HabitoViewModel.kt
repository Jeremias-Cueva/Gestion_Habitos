package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Habito
import com.example.gestionhabitos.model.repository.HabitoRepository
import kotlinx.coroutines.launch

class HabitoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = HabitoRepository(
        database.habitoDao(),
        database.categoriaDao(),
        database.registroHabitoDao()
    )

    private val _userEmail = MutableLiveData<String>()
    private var sincronizacionInicialRealizada = false

    val listaHabitos: LiveData<List<Habito>> = _userEmail.switchMap { email ->
        repository.obtenerHabitosDeUsuario(email).asLiveData()
    }

    fun cargarHabitosDeUsuario(email: String?) {
        if (email.isNullOrEmpty()) return
        if (_userEmail.value != email) {
            _userEmail.value = email
            sincronizacionInicialRealizada = false
        }
        if (!sincronizacionInicialRealizada) {
            sincronizacionInicialRealizada = true
            viewModelScope.launch {
                repository.sincronizarPendientes(email)
                repository.descargarHabitosDeNube(email)
            }
        }
    }

    fun insertar(habito: Habito) = viewModelScope.launch {
        repository.insertarHabito(habito)
    }

    // 🔥 NUEVA FUNCIÓN: Actualizar hábito completo (nombre, hora, etc)
    fun actualizar(habito: Habito) = viewModelScope.launch {
        repository.actualizarHabito(habito)
    }

    fun actualizarEstadoHabito(habito: Habito, isChecked: Boolean) {
        viewModelScope.launch {
            repository.actualizarHabito(habito.copy(completado = isChecked))
            repository.registrarActividad(habito.id, isChecked)
        }
    }

    fun eliminar(habito: Habito) = viewModelScope.launch {
        repository.eliminarHabito(habito)
    }
}