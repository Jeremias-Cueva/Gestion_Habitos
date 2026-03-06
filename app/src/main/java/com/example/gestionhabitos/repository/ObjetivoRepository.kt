package com.example.gestionhabitos.model.repository

import android.util.Log
import com.example.gestionhabitos.model.dao.ObjetivoDao
import com.example.gestionhabitos.model.entitis.Objetivo
import com.example.gestionhabitos.network.SupabaseClient
import kotlinx.coroutines.flow.Flow

class ObjetivoRepository(private val objetivoDao: ObjetivoDao) {

    fun obtenerObjetivosDeUsuario(email: String): Flow<List<Objetivo>> =
        objetivoDao.obtenerObjetivosPorUsuario(email)

    suspend fun insertarObjetivo(objetivo: Objetivo) {
        val idGenerado = objetivoDao.insertar(objetivo.copy(sincronizado = false))

        try {
            val response = SupabaseClient.apiService.insertarObjetivoEnNube(objetivo.copy(id = 0))

            if (response.isSuccessful) {
                val objetivoSincronizado = objetivo.copy(id = idGenerado.toInt(), sincronizado = true)
                objetivoDao.actualizar(objetivoSincronizado)
                Log.d("TESIS_SYNC", "✅ Objetivo sincronizado con éxito")
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "☁️ Sin conexión: Se guardó localmente")
        }
    }

    suspend fun descargarObjetivosDeNube(email: String) {
        try {
            val response = SupabaseClient.apiService.obtenerObjetivos("eq.$email")
            if (response.isSuccessful) {
                response.body()?.let { listaNube ->
                    // 👈 CORRECCIÓN: Este nombre debe ser IGUAL al del DAO
                    objetivoDao.guardarListaDeNube(listaNube)
                    Log.d("TESIS_SYNC", "📥 Se descargaron ${listaNube.size} objetivos")
                }
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "❌ Error al descargar de la nube: ${e.message}")
        }
    }

    suspend fun sincronizarPendientes(email: String) {
        try {
            val pendientes = objetivoDao.obtenerObjetivosPendientes(email)
            pendientes.forEach { obj ->
                val res = SupabaseClient.apiService.insertarObjetivoEnNube(obj.copy(id = 0))
                if (res.isSuccessful) {
                    objetivoDao.actualizar(obj.copy(sincronizado = true))
                    Log.d("TESIS_SYNC", "🔄 Sincronizado pendiente: ${obj.titulo}")
                }
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "⚠️ No se pudo sincronizar pendientes ahora")
        }
    }

    suspend fun actualizarObjetivo(objetivo: Objetivo) {
        objetivoDao.actualizar(objetivo)
    }

    suspend fun eliminarObjetivo(objetivo: Objetivo) {
        objetivoDao.eliminar(objetivo)
    }
}