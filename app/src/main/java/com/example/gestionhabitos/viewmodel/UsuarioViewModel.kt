package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Usuario
import com.example.gestionhabitos.model.repository.HabitoRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HabitoRepository
    // Observamos el primer usuario de la tabla (usualmente solo habrá uno)
    val datosUsuario: LiveData<Usuario?>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = HabitoRepository(db.habitoDao(), db.categoriaDao(), db.registroHabitoDao())
        // Suponiendo que el ID del usuario principal es 1
        datosUsuario = db.usuarioDao().obtenerUsuarioPorId(1).asLiveData()
    }

    fun actualizarUsuario(usuario: Usuario) = viewModelScope.launch {
        AppDatabase.getDatabase(getApplication()).usuarioDao().actualizar(usuario)
    }
}