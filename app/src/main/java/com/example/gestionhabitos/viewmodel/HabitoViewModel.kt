package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Habito
import kotlinx.coroutines.launch

class HabitoViewModel(application: Application) : AndroidViewModel(application) {
    private val habitoDao = AppDatabase.getDatabase(application).habitoDao()

    // Esta es la variable que el Fragmento intenta observar
    val listaHabitos: LiveData<List<Habito>> = habitoDao.obtenerTodosLosHabitos().asLiveData()

    fun insertar(habito: Habito) {
        viewModelScope.launch {
            habitoDao.insertar(habito)
        }
    }

    fun actualizar(habito: Habito) {
        viewModelScope.launch {
            habitoDao.actualizar(habito)
        }
    }

    fun eliminar(habito: Habito) {
        viewModelScope.launch {
            habitoDao.eliminar(habito)
        }
    }
}