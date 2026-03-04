package com.example.gestionhabitos.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Usuario
import com.example.gestionhabitos.model.api.RetrofitClient
import com.example.gestionhabitos.model.repository.RegistroRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val registroRepo = RegistroRepository()

    // Observa el usuario con ID 1 (Sesión activa en Room)
    val datosUsuario: LiveData<Usuario?> = db.usuarioDao().obtenerUsuarioPorId(1).asLiveData()

    // Estados para la UI
    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> get() = _loginResult

    private val _registroExitoso = MutableLiveData<Boolean?>()
    val registroExitoso: LiveData<Boolean?> get() = _registroExitoso

    // --- LÓGICA DE LOGIN ---
    fun login(emailIngresado: String, passwordIngresada: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            // Buscamos en MockAPI por el email (limpiando espacios)
            val response = RetrofitClient.habitFlow.buscarUsuarioPorEmail(emailIngresado.trim())

            if (response.isSuccessful) {
                val listaUsuarios = response.body()

                // Filtro manual estricto para evitar errores de MockAPI
                val usuarioEncontrado = listaUsuarios?.find {
                    it.email.trim().lowercase() == emailIngresado.trim().lowercase() &&
                            it.password.trim() == passwordIngresada.trim()
                }

                if (usuarioEncontrado != null) {
                    // Guardamos en Room forzando el ID 1 para activar el Auto-Login
                    val usuarioParaSesion = usuarioEncontrado.copy(id = 1)
                    db.usuarioDao().insertar(usuarioParaSesion)
                    _loginResult.postValue(true)
                } else {
                    _loginResult.postValue(false)
                }
            } else {
                _loginResult.postValue(false)
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error en login: ${e.message}")
            _loginResult.postValue(false)
        }
    }

    // --- LÓGICA DE REGISTRO ---
    fun registrar(nombre: String, email: String, pass: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            // Creamos el objeto sin ID (MockAPI lo genera automáticamente)
            val nuevoUsuario = Usuario(
                nombre = nombre,
                email = email.trim(),
                password = pass.trim()
            )

            val response = registroRepo.registrarUsuario(nuevoUsuario)

            if (response.isSuccessful) {
                _registroExitoso.postValue(true)
            } else {
                Log.e("API_ERROR", "Error de servidor: ${response.code()}")
                _registroExitoso.postValue(false)
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Falla de red: ${e.message}")
            _registroExitoso.postValue(false)
        }
    }

    // --- LÓGICA DE CIERRE DE SESIÓN ---
    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        // Borra el registro del ID 1 para que el Auto-Login pida credenciales de nuevo
        db.usuarioDao().borrarPorId(1)
    }

    // --- LIMPIEZA DE ESTADOS ---
    fun resetLoginResult() { _loginResult.postValue(null) }
    fun resetRegistroStatus() { _registroExitoso.postValue(null) }

    // Actualización de perfil
    fun actualizarUsuario(usuario: Usuario) = viewModelScope.launch(Dispatchers.IO) {
        db.usuarioDao().actualizar(usuario)
    }
}