package com.example.gestionhabitos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.dao.ProgresoDiario
import com.example.gestionhabitos.model.entitis.Habito

class EstadisticasViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val habitoDao = db.habitoDao()
    private val registroDao = db.registroHabitoDao()

    // 1. LiveData para armazenar o email do usuário ativo
    private val userEmail = MutableLiveData<String>()

    // 2. Usamos switchMap para buscar apenas os hábitos do usuário logado
    val listaHabitos: LiveData<List<Habito>> = userEmail.switchMap { email ->
        habitoDao.obtenerHabitosPorUsuario(email).asLiveData()
    }

    // 3. Estatísticas baseadas na lista filtrada
    val estadisticasProgreso: LiveData<Pair<Int, Int>> = listaHabitos.map { habitos ->
        val total = habitos?.size ?: 0
        val completados = habitos?.count { it.completado } ?: 0
        Pair(completados, total)
    }

    // 4. Progresso Semanal (Note que se o seu registroDao também precisar de filtro por email,
    // você deve aplicar a mesma lógica de switchMap aqui)
    val progresoSemanal: LiveData<List<ProgresoDiario>> = registroDao.obtenerProgresoSemanal().asLiveData()

    // Método para carregar o email do usuário a partir do Fragment
    fun cargarDatosUsuario(email: String) {
        userEmail.value = email
    }
}