package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.RegistroHabito
import kotlinx.coroutines.launch

class RegistroViewModel(application: Application) : AndroidViewModel(application) {
    private val registroDao = AppDatabase.getDatabase(application).registroHabitoDao()

    // Obtener registros de un hábito específico para gráficas de constancia
    fun obtenerRegistrosPorHabito(habitoId: Int): LiveData<List<RegistroHabito>> {
        return registroDao.obtenerRegistrosPorHabito(habitoId).asLiveData()
    }

    fun registrarCumplimiento(registro: RegistroHabito) {
        viewModelScope.launch {
            registroDao.insertar(registro)
        }
    }
}