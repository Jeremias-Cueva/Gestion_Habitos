package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Usuario
import com.example.gestionhabitos.model.repository.HabitoRepository
import kotlinx.coroutines.launch
import com.example.gestionhabitos.model.api.RetrofitClient
import kotlinx.coroutines.Dispatchers

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HabitoRepository
    val datosUsuario: LiveData<Usuario?>

    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> get() = _loginResult

    private val _registroExitoso = MutableLiveData<Boolean?>()
    val registroExitoso: LiveData<Boolean?> get() = _registroExitoso

    init {
        val db = AppDatabase.getDatabase(application)
        repository = HabitoRepository(db.habitoDao(), db.categoriaDao(), db.registroHabitoDao())
        // Observamos siempre el ID 1 para mantener la sesión dinámica
        datosUsuario = db.usuarioDao().obtenerUsuarioPorId(1).asLiveData()
    }

    fun registrar(nombre: String, email: String, pass: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            // 1. Enviamos a MockAPI (ID 0 para que la nube genere uno nuevo)
            val nuevoUsuario = Usuario(0, nombre, email, pass, "")
            val response = RetrofitClient.habitFlow.registrarUsuario(nuevoUsuario)

            if (response.isSuccessful) {
                val usuarioCreado = response.body()
                if (usuarioCreado != null) {
                    // 2. FORZAMOS EL ID 1: Esto es lo que activa el nombre en las otras pantallas
                    val usuarioSesion = usuarioCreado.copy(id = 1)
                    AppDatabase.getDatabase(getApplication()).usuarioDao().insertar(usuarioSesion)
                    _registroExitoso.postValue(true)
                }
            } else {
                _registroExitoso.postValue(false)
            }
        } catch (e: Exception) {
            _registroExitoso.postValue(false)
        }
    }

    fun login(emailIngresado: String, passwordIngresada: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = RetrofitClient.habitFlow.buscarUsuarioPorEmail(emailIngresado)
            if (response.isSuccessful) {
                val usuarioEncontrado = response.body()?.firstOrNull {
                    it.email == emailIngresado && it.password == passwordIngresada
                }
                if (usuarioEncontrado != null) {
                    val usuarioSesion = usuarioEncontrado.copy(id = 1)
                    AppDatabase.getDatabase(getApplication()).usuarioDao().insertar(usuarioSesion)
                    _loginResult.postValue(true)
                } else {
                    _loginResult.postValue(false)
                }
            }
        } catch (e: Exception) {
            _loginResult.postValue(false)
        }
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        val db = AppDatabase.getDatabase(getApplication())
        val usuarioActual = db.usuarioDao().obtenerUsuarioPorIdDirecto(1)
        usuarioActual?.let { db.usuarioDao().eliminar(it) }
    }

    fun resetLoginResult() { _loginResult.value = null }
    fun resetRegistroResult() { _registroExitoso.value = null }

    fun actualizarUsuario(usuario: Usuario) = viewModelScope.launch {
        AppDatabase.getDatabase(getApplication()).usuarioDao().actualizar(usuario)
    }
}