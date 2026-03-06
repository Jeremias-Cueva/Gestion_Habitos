package com.example.gestionhabitos.model.repository

import android.util.Log
import com.example.gestionhabitos.model.dao.*
import com.example.gestionhabitos.model.entitis.*
import com.example.gestionhabitos.network.SupabaseClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class HabitoRepository(
    private val habitoDao: HabitoDao,
    private val categoriaDao: CategoriaDao,
    private val registroDao: RegistroHabitoDao
) {
    fun obtenerHabitosDeUsuario(email: String): Flow<List<Habito>> =
        habitoDao.obtenerHabitosPorUsuario(email)

    // 📥 DESCARGAR: Trae lo de la nube al celular (Evita duplicados por nombre)
    suspend fun descargarHabitosDeNube(email: String) {
        try {
            val response = SupabaseClient.apiService.obtenerHabitos("eq.$email")
            if (response.isSuccessful && response.body() != null) {
                val habitosNube = response.body()!!
                val habitosActuales = habitoDao.obtenerHabitosPorUsuario(email).first()
                val nombresLocales = habitosActuales.map { it.nombre }

                habitosNube.forEach { habitoNube ->
                    if (!nombresLocales.contains(habitoNube.nombre)) {
                        habitoDao.insertar(habitoNube)
                    }
                }
                Log.d("TESIS_SYNC", "✅ Descarga exitosa. Total en nube: ${habitosNube.size}")
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "❌ Error al descargar: ${e.message}")
        }
    }

    // 📤 INSERTAR: Intenta subir al momento. Si falla, queda en Room para después.
    suspend fun insertarHabito(habito: Habito) {
        habitoDao.insertar(habito)
        try {
            val response = SupabaseClient.apiService.insertarHabito(habito)
            if (response.isSuccessful) {
                Log.d("TESIS_SYNC", "✅ Sincronizado al instante")
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "☁️ Guardado en local (Offline)")
        }
    }

    // 🔄 RECONCILIACIÓN: La pieza que te faltaba para el modo Offline
    suspend fun sincronizarHabitosPendientes(email: String) {
        try {
            // 1. Ver qué hay en el celular
            val habitosLocales = habitoDao.obtenerHabitosPorUsuario(email).first()

            // 2. Ver qué hay en la nube
            val responseNube = SupabaseClient.apiService.obtenerHabitos("eq.$email")

            if (responseNube.isSuccessful && responseNube.body() != null) {
                val habitosEnNube = responseNube.body()!!
                val nombresEnNube = habitosEnNube.map { it.nombre }

                // 3. Comparar: Si el nombre local no está en la nube, se sube
                habitosLocales.forEach { local ->
                    if (!nombresEnNube.contains(local.nombre)) {
                        Log.d("TESIS_SYNC", "📤 Subiendo pendiente: ${local.nombre}")
                        SupabaseClient.apiService.insertarHabito(local)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "❌ Falló reconciliación: ${e.message}")
        }
    }

    suspend fun actualizarHabito(habito: Habito) = habitoDao.actualizar(habito)
    suspend fun eliminarHabito(habito: Habito) = habitoDao.eliminar(habito)
    val todasLasCategorias: Flow<List<Categoria>> = categoriaDao.obtenerCategorias()

    suspend fun registrarActividad(habitoId: Int, estaCompletado: Boolean) {
        val nuevoRegistro = RegistroHabito(
            habitoId = habitoId,
            fecha = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
            completado = estaCompletado
        )
        registroDao.insertar(nuevoRegistro)
        try {
            SupabaseClient.apiService.insertarRegistro(nuevoRegistro)
        } catch (e: Exception) {
            Log.e("TESIS_SYNC", "Fallo sync registro")
        }
    }
}