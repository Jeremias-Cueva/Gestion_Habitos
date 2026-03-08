package com.example.gestionhabitos.model.repository

import android.util.Log
import com.example.gestionhabitos.model.dao.ObjetivoDao
import com.example.gestionhabitos.model.entitis.Objetivo
import com.example.gestionhabitos.network.SupabaseClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ObjetivoRepository(private val objetivoDao: ObjetivoDao) {

    fun obtenerObjetivosDeUsuario(email: String): Flow<List<Objetivo>> =
        objetivoDao.obtenerObjetivosPorUsuario(email)

    suspend fun descargarObjetivosDeNube(email: String) {
        try {
            val response = SupabaseClient.apiService.obtenerObjetivos("eq.$email")
            if (response.isSuccessful) {
                val objetivosNube = response.body() ?: emptyList()
                val objetivosLocales = objetivoDao.obtenerObjetivosPorUsuario(email).first()

                objetivosNube.forEach { nube ->
                    // 🔥 ANTI-DUPLICADO: Buscamos coincidencias por Título
                    val existeLocal = objetivosLocales.find { 
                        it.titulo == nube.titulo && !it.sincronizado 
                    }
                    if (existeLocal != null) {
                        objetivoDao.eliminar(existeLocal)
                    }
                    objetivoDao.insertar(nube.copy(sincronizado = true))
                }
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "Error descarga objetivos: ${e.message}")
        }
    }

    suspend fun insertarObjetivo(objetivo: Objetivo) {
        // 1. Guardar local siempre (Garantiza Modo Offline)
        val idLocal = objetivoDao.insertar(objetivo.copy(id = 0, sincronizado = false))
        val objetivoLocal = objetivo.copy(id = idLocal.toInt(), sincronizado = false)

        try {
            // 2. Intentar sincronizar si hay red
            val response = SupabaseClient.apiService.insertarObjetivoEnNube(objetivo.copy(id = 0))
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val confirmado = response.body()!![0]
                // 3. Reemplazo seguro de temporal por oficial
                objetivoDao.eliminar(objetivoLocal)
                objetivoDao.insertar(confirmado.copy(sincronizado = true))
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "Trabajando en modo Offline")
        }
    }

    suspend fun sincronizarPendientes(email: String) {
        try {
            val pendientes = objetivoDao.obtenerObjetivosPendientes(email)
            pendientes.forEach { obj ->
                val response = SupabaseClient.apiService.insertarObjetivoEnNube(obj.copy(id = 0))
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    val confirmado = response.body()!![0]
                    objetivoDao.eliminar(obj)
                    objetivoDao.insertar(confirmado.copy(sincronizado = true))
                }
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "Sync pendientes objetivos falló")
        }
    }

    suspend fun actualizarObjetivo(objetivo: Objetivo) {
        objetivoDao.actualizar(objetivo)
        try {
            SupabaseClient.apiService.actualizarObjetivoEnNube("eq.${objetivo.id}", objetivo)
        } catch (e: Exception) { }
    }

    suspend fun eliminarObjetivo(objetivo: Objetivo) {
        objetivoDao.eliminar(objetivo)
        try {
            SupabaseClient.apiService.eliminarObjetivoEnNube("eq.${objetivo.id}")
        } catch (e: Exception) { }
    }
}