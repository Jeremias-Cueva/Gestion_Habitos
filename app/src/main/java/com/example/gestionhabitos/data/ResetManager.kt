package com.example.gestionhabitos.data

import android.content.Context
import com.example.gestionhabitos.database.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

class ResetManager(context: Context) {

    private val prefs = context.getSharedPreferences("reset_prefs", Context.MODE_PRIVATE)
    private val db = AppDatabase.getDatabase(context)

    suspend fun ejecutarReinicioDiario(email: String?) {
        if (email == null) return

        val hoy = getFechaDeHoy()
        val ultimaFecha = prefs.getString("ultima_fecha_reinicio", null)

        if (hoy != ultimaFecha) {
            // Es un nuevo día, reiniciamos
            db.habitoDao().reiniciarHabitosDiarios(email)
            db.objetivoDao().reiniciarObjetivosDiarios(email)
            
            // Guardamos la fecha de hoy para no volver a reiniciar
            prefs.edit().putString("ultima_fecha_reinicio", hoy).apply()
        }
    }

    private fun getFechaDeHoy(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}