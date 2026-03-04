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
import kotlinx.coroutines.withContext

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val registroRepo = RegistroRepository()

    // Siempre observa el ID 1 para la sesión
    val datosUsuario: LiveData<Usuario?> = db.usuarioDao().obtenerSesionActiva().asLiveData()

    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> get() = _loginResult

    private val _registroExitoso = MutableLiveData<Boolean?>()
    val registroExitoso: LiveData<Boolean?> get() = _registroExitoso

    fun login(email: String, pass: String) = viewModelScope.launch {
        try {
            // 1. Petición de red en hilo IO
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.habitFlow.buscarUsuarioPorEmail(email.trim())
            }

            if (response.isSuccessful) {
                val user = response.body()?.find {
                    it.email.equals(email.trim(), true) && it.password == pass.trim()
                }

                if (user != null) {
                    // 2. FORZAR guardado en Room y ESPERAR a que termine
                    withContext(Dispatchers.IO) {
                        db.usuarioDao().borrarPorId(1)
                        db.usuarioDao().insertar(user.copy(id = 1))
                    }
                    // 3. Solo después de guardar, notificamos éxito
                    _loginResult.value = true
                } else {
                    _loginResult.value = false
                }
            } else {
                _loginResult.value = false
            }
        } catch (e: Exception) {
            Log.e("LOGIN_ERROR", e.message ?: "Error desconocido")
            _loginResult.value = false
        }
    }

    fun registrar(nombre: String, email: String, pass: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val nuevoUsuario = Usuario(nombre = nombre, email = email.trim(), password = pass.trim())
            val response = registroRepo.registrarUsuario(nuevoUsuario)
            _registroExitoso.postValue(response.isSuccessful)
        } catch (e: Exception) {
            _registroExitoso.postValue(false)
        }
    }

    fun actualizarUsuario(usuario: Usuario) = viewModelScope.launch(Dispatchers.IO) {
        db.usuarioDao().actualizar(usuario)
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        db.usuarioDao().borrarPorId(1)
    }

    fun resetLoginResult() { _loginResult.value = null }
    fun resetRegistroStatus() { _registroExitoso.value = null }
}