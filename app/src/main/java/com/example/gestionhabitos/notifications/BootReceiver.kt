package com.example.gestionhabitos.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.gestionhabitos.database.AppDatabase
import com.example.gestionhabitos.model.entitis.Habito
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val alarmHelper = AlarmHelper(context)
            val db = AppDatabase.getDatabase(context)
            
            CoroutineScope(Dispatchers.IO).launch {
                // Obtenemos al usuario activo para saber de quién cargar los hábitos
                val usuario = db.usuarioDao().obtenerSesionActiva().first()
                usuario?.let { user ->
                    val habitos: List<Habito> = db.habitoDao().obtenerHabitosPorUsuario(user.email).first()
                    habitos.forEach { habito ->
                        if (habito.hora.isNotEmpty()) {
                            alarmHelper.programarAlarma(habito)
                        }
                    }
                }
            }
        }
    }
}