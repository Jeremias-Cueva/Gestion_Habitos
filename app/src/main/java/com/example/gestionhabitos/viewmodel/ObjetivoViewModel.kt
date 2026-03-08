package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Objetivo
import com.example.gestionhabitos.model.repository.ObjetivoRepository
import kotlinx.coroutines.launch

class ObjetivoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ObjetivoRepository
    private val usuarioEmail: LiveData<String?>

    val listaObjetivos: LiveData<List<Objetivo>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ObjetivoRepository(db.objetivoDao())
        
        // 1. Obtenemos el email del usuario activo
        usuarioEmail = db.usuarioDao().obtenerSesionActiva().asLiveData().map { it?.email }
        
        // 2. Cargamos los objetivos asociados (Room se encarga de mantener esto actualizado)
        listaObjetivos = usuarioEmail.switchMap { email ->
            if (email != null) {
                // 🔥 TRUCO: Aprovechamos que ya tenemos el email para disparar la sincronización
                sincronizarAhora(email) 
                repository.obtenerObjetivosDeUsuario(email).asLiveData()
            } else {
                MutableLiveData(emptyList())
            }
        }
    }

    private fun sincronizarAhora(email: String) {
        viewModelScope.launch {
            repository.sincronizarPendientes(email)
            repository.descargarObjetivosDeNube(email)
        }
    }

    fun agregarObjetivo(titulo: String, descripcion: String, meta: Double) {
        val email = usuarioEmail.value ?: return
        val nuevo = Objetivo(
            titulo = titulo,
            descripcion = descripcion,
            metaValor = meta,
            usuarioEmail = email
        )
        viewModelScope.launch {
            repository.insertarObjetivo(nuevo)
        }
    }

    fun actualizarProgreso(objetivo: Objetivo, nuevoValor: Double) {
        viewModelScope.launch {
            val actualizado = objetivo.copy(
                valorActual = nuevoValor,
                completado = nuevoValor >= objetivo.metaValor
            )
            repository.actualizarObjetivo(actualizado)
        }
    }

    fun eliminarObjetivo(objetivo: Objetivo) {
        viewModelScope.launch {
            repository.eliminarObjetivo(objetivo)
        }
    }

    // Mantenemos esta función por si quieres un botón de "Refrescar" manual
    fun sincronizarConNube() {
        usuarioEmail.value?.let { sincronizarAhora(it) }
    }
}