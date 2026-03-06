package com.example.gestionhabitos.model.repository

import android.util.Log
import com.example.gestionhabitos.model.dao.*
import com.example.gestionhabitos.model.entitis.*
import com.example.gestionhabitos.network.SupabaseClient
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class HabitoRepository(
    private val habitoDao: HabitoDao,
    private val categoriaDao: CategoriaDao,
    private val registroDao: RegistroHabitoDao
) {

    fun obtenerHabitosDeUsuario(email: String): Flow<List<Habito>> =
        habitoDao.obtenerHabitosPorUsuario(email)

    suspend fun descargarHabitosDeNube(email: String) {
        try {
            val response = SupabaseClient.apiService.obtenerHabitos("eq.$email")
            if (response.isSuccessful) {
                val habitosNube = response.body() ?: emptyList()
                habitosNube.forEach { habitoNube ->
                    // Usamos insertarOActualizar para no duplicar ni machacar datos locales
                    habitoDao.insertarOActualizar(habitoNube.copy(sincronizado = true))
                }
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "Error descarga: ${e.message}")
        }
    }

    suspend fun insertarHabito(habito: Habito) {
        // 1. Guardar local siempre primero (Garantiza que el usuario lo vea)
        val idLocal = habitoDao.insertar(habito.copy(id = 0, sincronizado = false))
        val habitoTemporal = habito.copy(id = idLocal.toInt(), sincronizado = false)

        try {
            // 2. Intentar subir a Supabase
            val response = SupabaseClient.apiService.insertarHabito(habito.copy(id = 0))
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val confirmado = response.body()!![0]

                // 3. Reemplazo de ID local por ID de nube
                habitoDao.eliminar(habitoTemporal)
                habitoDao.insertar(confirmado.copy(sincronizado = true))

                // 🔥 LA CLAVE: Ya que hay internet, intentamos subir el resto que esté pendiente
                sincronizarPendientes(habito.usuarioEmail)
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "Offline: Se mantiene local con ID $idLocal")
        }
    }

    suspend fun sincronizarPendientes(email: String) {
        try {
            val pendientes = habitoDao.obtenerHabitosPendientes(email)
            if (pendientes.isEmpty()) return

            pendientes.forEach { offline ->
                // Enviamos con ID 0 para que Supabase genere su propio ID
                val response = SupabaseClient.apiService.insertarHabito(offline.copy(id = 0))
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    val confirmado = response.body()!![0]
                    // Borramos el registro offline y ponemos el de la nube
                    habitoDao.eliminar(offline)
                    habitoDao.insertar(confirmado.copy(sincronizado = true))
                    Log.d("TESIS_SYNC", "Hábito pendiente sincronizado: ${confirmado.nombre}")
                }
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "Sync pendientes falló: sigue offline")
        }
    }

    suspend fun actualizarHabito(habito: Habito) {
        habitoDao.actualizar(habito)
        try {
            SupabaseClient.apiService.actualizarHabitoEnNube("eq.${habito.id}", habito)
        } catch (e: Exception) { Log.e("TESIS_SYNC", "Update solo local") }
    }

    suspend fun eliminarHabito(habito: Habito) {
        habitoDao.eliminar(habito)
        try {
            SupabaseClient.apiService.eliminarHabitoEnNube("eq.${habito.id}")
        } catch (e: Exception) { Log.e("TESIS_SYNC", "Delete solo local") }
    }

    suspend fun registrarActividad(habitoId: Int, estaCompletado: Boolean) {
        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val reg = RegistroHabito(habitoId = habitoId, fecha = fecha, completado = estaCompletado)
        registroDao.insertar(reg)
        try { SupabaseClient.apiService.insertarRegistro(reg) } catch (e: Exception) {}
    }
}