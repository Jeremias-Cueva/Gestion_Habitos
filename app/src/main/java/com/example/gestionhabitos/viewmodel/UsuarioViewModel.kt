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
    // ¡Adiós RegistroRepository y RetrofitClient! Usamos nuestra única fuente de la verdad
    private val usuarioRepo = UsuarioRepository()

    val datosUsuario: LiveData<Usuario?> = db.usuarioDao().obtenerSesionActiva().asLiveData()

    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> get() = _loginResult

    private val _registroExitoso = MutableLiveData<Boolean?>()
    val registroExitoso: LiveData<Boolean?> get() = _registroExitoso

    private val _errorRegistro = MutableLiveData<String?>()
    val errorRegistro: LiveData<String?> get() = _errorRegistro

    fun login(email: String, pass: String) = viewModelScope.launch {
        try {
            val response = withContext(Dispatchers.IO) {
                usuarioRepo.buscarUsuarioPorEmail(email.trim())
            }

            if (response.isSuccessful) {
                val user = response.body()?.find {
                    it.password == pass.trim()
                }

                if (user != null) {
                    withContext(Dispatchers.IO) {
                        db.usuarioDao().borrarPorId(1)
                        // Guardamos en Room con ID 1 para mantener tu lógica de sesión local
                        db.usuarioDao().insertar(user.copy(id = 1))
                    }
                    _loginResult.value = true
                } else {
                    _loginResult.value = false
                }
            } else {
                _loginResult.value = false
            }
        } catch (e: Exception) {
            Log.e("TESIS_LOGIN", e.message ?: "Error desconocido")
            _loginResult.value = false
        }
    }

    fun registrar(nombre: String, email: String, pass: String) = viewModelScope.launch {
        try {
            _errorRegistro.value = null

            // 1. VERIFICAR SI EL CORREO YA EXISTE EN SUPABASE
            val checkResponse = withContext(Dispatchers.IO) {
                usuarioRepo.buscarUsuarioPorEmail(email.trim())
            }

            if (checkResponse.isSuccessful && !checkResponse.body().isNullOrEmpty()) {
                _errorRegistro.value = "Este correo ya está registrado. Intenta con otro."
                _registroExitoso.value = false
                return@launch
            }

            // 2. GENERAR ID ÚNICO PARA LA NUBE (Para que no choquen en Supabase)
            val idUnicoParaNube = (1000..999999).random()
            val nuevoUsuario = Usuario(
                id = idUnicoParaNube,
                nombre = nombre,
                email = email.trim(),
                password = pass.trim(),
                fotoUri = ""
            )

            // 3. ENVIAR A SUPABASE
            val response = withContext(Dispatchers.IO) {
                usuarioRepo.registrarEnNube(nuevoUsuario)
            }

            _registroExitoso.value = response.isSuccessful

        } catch (e: Exception) {
            Log.e("TESIS_REGISTRO", "Fallo red: ${e.message}")
            _errorRegistro.value = "Error de conexión al intentar registrar."
            _registroExitoso.value = false
        }
    }

    fun actualizarUsuario(usuario: Usuario) = viewModelScope.launch(Dispatchers.IO) {
        db.usuarioDao().actualizar(usuario)
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        db.usuarioDao().borrarPorId(1)
    }

    fun resetLoginResult() { _loginResult.value = null }
    fun resetRegistroStatus() {
        _registroExitoso.value = null
        _errorRegistro.value = null
    }
}