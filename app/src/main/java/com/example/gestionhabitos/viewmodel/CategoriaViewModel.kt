package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Categoria
import kotlinx.coroutines.launch

class CategoriaViewModel(application: Application) : AndroidViewModel(application) {
    private val categoriaDao = AppDatabase.getDatabase(application).categoriaDao()

    // Observamos todas las categorías disponibles en la DB
    val todasLasCategorias: LiveData<List<Categoria>> = categoriaDao.obtenerCategorias().asLiveData()

    fun insertar(categoria: Categoria) {
        viewModelScope.launch {
            categoriaDao.insertar(categoria)
        }
    }

    fun eliminar(categoria: Categoria) {
        viewModelScope.launch {
            categoriaDao.eliminar(categoria)
        }
    }
}