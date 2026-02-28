package com.example.gestionhabitos.model.repository

import com.example.gestionhabitos.model.dao.RegistroHabitoDao
import com.example.gestionhabitos.model.dao.CategoriaDao
import com.example.gestionhabitos.model.dao.HabitoDao
import com.example.gestionhabitos.model.entitis.Habito
import com.example.gestionhabitos.model.entitis.Categoria
import com.example.gestionhabitos.model.entitis.RegistroHabito
import kotlinx.coroutines.flow.Flow

class HabitoRepository(
    private val habitoDao: HabitoDao,
    private val categoriaDao: CategoriaDao,
    private val registroDao: RegistroHabitoDao
) {
    // Operaciones de Hábitos
    val todosLosHabitos: Flow<List<Habito>> = habitoDao.obtenerTodosLosHabitos()

    suspend fun insertarHabito(habito: Habito) = habitoDao.insertar(habito)
    suspend fun actualizarHabito(habito: Habito) = habitoDao.actualizar(habito)
    suspend fun eliminarHabito(habito: Habito) = habitoDao.eliminar(habito)

    // Operaciones de Categorías
    val todasLasCategorias: Flow<List<Categoria>> = categoriaDao.obtenerCategorias()

    // Operaciones de Registros (Historial)
    fun obtenerRegistros(habitoId: Int): Flow<List<RegistroHabito>> =
        registroDao.obtenerRegistrosPorHabito(habitoId)

    suspend fun insertarRegistro(registro: RegistroHabito) = registroDao.insertar(registro)
    // Dentro de HabitoRepository.kt
    suspend fun registrarActividad(habitoId: Int, estaCompletado: Boolean) {
        val nuevoRegistro = RegistroHabito(
            habitoId = habitoId,
            fecha = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
            completado = estaCompletado
        )
        registroDao.insertar(nuevoRegistro)
    }
}