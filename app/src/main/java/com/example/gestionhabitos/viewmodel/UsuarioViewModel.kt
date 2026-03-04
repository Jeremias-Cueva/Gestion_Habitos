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

    // LiveData para que el Fragment sepa si el login fue exitoso
    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> get() = _loginResult

    init {
        val db = AppDatabase.getDatabase(application)
        repository = HabitoRepository(db.habitoDao(), db.categoriaDao(), db.registroHabitoDao())
        // Observamos siempre al usuario con ID 1 (el usuario activo)
        datosUsuario = db.usuarioDao().obtenerUsuarioPorId(1).asLiveData()
    }

    fun actualizarUsuario(usuario: Usuario) = viewModelScope.launch {
        AppDatabase.getDatabase(getApplication()).usuarioDao().actualizar(usuario)
    }

    fun login(emailIngresado: String, passwordIngresada: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = RetrofitClient.habitFlow.buscarUsuarioPorEmail(emailIngresado)

            if (response.isSuccessful) {
                val listaUsuarios = response.body()
                val usuarioEncontrado = listaUsuarios?.firstOrNull {
                    it.email == emailIngresado && it.password == passwordIngresada
                }

                if (usuarioEncontrado != null) {
                    // Sincronizamos con Room: Forzamos el ID 1 para que sea el usuario de la sesión
                    val usuarioParaSesion = usuarioEncontrado.copy(id = 1)
                    AppDatabase.getDatabase(getApplication()).usuarioDao().actualizar(usuarioParaSesion)

                    _loginResult.postValue(true)
                } else {
                    _loginResult.postValue(false)
                }
            } else {
                _loginResult.postValue(false)
            }
        } catch (e: Exception) {
            _loginResult.postValue(false)
        }
    }

    // Función para limpiar el resultado del login después de usarlo
    fun resetLoginResult() { _loginResult.value = null }

    // Agrega esto a tu UsuarioViewModel.kt
    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        // Borramos al usuario de la sesión (el que tiene ID 1)
        val db = AppDatabase.getDatabase(getApplication())
        val usuarioActual = db.usuarioDao().obtenerUsuarioPorIdDirecto(1) // Necesitas este método en tu DAO
        usuarioActual?.let {
            db.usuarioDao().eliminar(it)
        }
    }
}