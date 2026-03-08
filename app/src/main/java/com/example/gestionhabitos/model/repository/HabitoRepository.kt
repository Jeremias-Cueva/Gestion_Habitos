package com.example.gestionhabitos.model.repository

import android.util.Log
import com.example.gestionhabitos.model.dao.*
import com.example.gestionhabitos.model.entitis.*
import com.example.gestionhabitos.network.SupabaseClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
                val habitosLocales = habitoDao.obtenerHabitosPorUsuario(email).first()

                habitosNube.forEach { nube ->
                    // 🔥 EVITAR DUPLICADOS: Buscamos si ya existe uno local con el mismo nombre y hora
                    val existeLocal = habitosLocales.find { 
                        it.nombre == nube.nombre && it.hora == nube.hora && !it.sincronizado 
                    }
                    
                    if (existeLocal != null) {
                        // Si existe, borramos el local "offline" y ponemos el de la "nube" (que ya tiene ID oficial)
                        habitoDao.eliminar(existeLocal)
                    }
                    habitoDao.insertarOActualizar(nube.copy(sincronizado = true))
                }
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "Error descarga: ${e.message}")
        }
    }

    suspend fun insertarHabito(habito: Habito) {
        // Guardar local siempre primero
        val idLocal = habitoDao.insertar(habito.copy(id = 0, sincronizado = false))
        val habitoLocal = habito.copy(id = idLocal.toInt(), sincronizado = false)

        try {
            val response = SupabaseClient.apiService.insertarHabito(habito.copy(id = 0))
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val confirmado = response.body()!![0]
                // Reemplazamos el temporal por el oficial de la nube
                habitoDao.eliminar(habitoLocal)
                habitoDao.insertar(confirmado.copy(sincronizado = true))
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "Offline: Se mantiene local")
        }
    }

    suspend fun sincronizarPendientes(email: String) {
        try {
            val pendientes = habitoDao.obtenerHabitosPendientes(email)
            pendientes.forEach { offline ->
                val response = SupabaseClient.apiService.insertarHabito(offline.copy(id = 0))
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    val confirmado = response.body()!![0]
                    habitoDao.eliminar(offline)
                    habitoDao.insertar(confirmado.copy(sincronizado = true))
                }
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "Sync pendientes falló")
        }
    }

    suspend fun actualizarHabito(habito: Habito) {
        habitoDao.actualizar(habito)
        try {
            SupabaseClient.apiService.actualizarHabitoEnNube("eq.${habito.id}", habito)
        } catch (e: Exception) { }
    }

    suspend fun eliminarHabito(habito: Habito) {
        habitoDao.eliminar(habito)
        try {
            SupabaseClient.apiService.eliminarHabitoEnNube("eq.${habito.id}")
        } catch (e: Exception) { }
    }

    suspend fun registrarActividad(habitoId: Int, estaCompletado: Boolean) {
        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val reg = RegistroHabito(habitoId = habitoId, fecha = fecha, completado = estaCompletado)
        registroDao.insertar(reg)
        try { SupabaseClient.apiService.insertarRegistro(reg) } catch (e: Exception) {}
    }
}