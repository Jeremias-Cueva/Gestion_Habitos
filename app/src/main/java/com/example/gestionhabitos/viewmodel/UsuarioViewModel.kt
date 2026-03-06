package com.example.gestionhabitos.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Usuario
import com.example.gestionhabitos.repository.UsuarioRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val usuarioRepo = UsuarioRepository()

    val datosUsuario: LiveData<Usuario?> = db.usuarioDao().obtenerSesionActiva().asLiveData()

    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> get() = _loginResult

    private val _registroExitoso = MutableLiveData<Boolean?>()
    val registroExitoso: LiveData<Boolean?> get() = _registroExitoso

    private val _errorRegistro = MutableLiveData<String?>()
    val errorRegistro: LiveData<String?> get() = _errorRegistro

    // --- LOGIN ---
    fun login(email: String, pass: String) = viewModelScope.launch {
        try {
            val response = withContext(Dispatchers.IO) {
                usuarioRepo.buscarUsuarioPorEmail(email.trim())
            }

            if (response.isSuccessful) {
                val user = response.body()?.find { it.password == pass.trim() }
                if (user != null) {
                    withContext(Dispatchers.IO) {
                        db.usuarioDao().borrarSesion()
                        db.usuarioDao().insertar(user)
                    }
                    _loginResult.value = true
                } else { _loginResult.value = false }
            } else { _loginResult.value = false }
        } catch (e: Exception) {
            _loginResult.value = false
        }
    }

    // --- REGISTRO (CORREGIDO PARA GUARDAR EN LOCAL TAMBIÉN) ---
    fun registrar(nombre: String, email: String, pass: String) = viewModelScope.launch {
        try {
            _errorRegistro.value = null

            // 1. Verificar si existe en la nube
            val checkResponse = withContext(Dispatchers.IO) {
                usuarioRepo.buscarUsuarioPorEmail(email.trim())
            }

            if (checkResponse.isSuccessful && !checkResponse.body().isNullOrEmpty()) {
                _errorRegistro.value = "Este correo ya está registrado."
                _registroExitoso.value = false
                return@launch
            }

            // 2. Crear objeto usuario
            val nuevoUsuario = Usuario(
                nombre = nombre,
                email = email.trim(),
                password = pass.trim()
            )

            // 3. Enviar a Supabase
            val response = withContext(Dispatchers.IO) {
                usuarioRepo.registrarEnNube(nuevoUsuario)
            }

            if (response.isSuccessful) {

                // 🚨 NO GUARDAMOS SESIÓN AQUÍ
                // Solo confirmamos registro

                _registroExitoso.value = true

            } else {
                _errorRegistro.value = "Error al guardar en la nube."
                _registroExitoso.value = false
            }

        } catch (e: Exception) {
            Log.e("TESIS_REGISTRO", "Fallo: ${e.message}")
            _errorRegistro.value = "Error de conexión."
            _registroExitoso.value = false
        }
    }

    fun actualizarUsuario(usuario: Usuario) = viewModelScope.launch(Dispatchers.IO) {
        db.usuarioDao().actualizar(usuario)
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        db.usuarioDao().borrarSesion()
    }

    fun resetLoginResult() { _loginResult.value = null }
    fun resetRegistroStatus() {
        _registroExitoso.value = null
        _errorRegistro.value = null
    }
}