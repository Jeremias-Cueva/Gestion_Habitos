package com.example.gestionhabitos.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.example.gestionhabitos.model.entitis.Habito
import java.util.*

class AlarmHelper(private val context: Context) {

    fun programarAlarma(habito: Habito) {
        if (habito.hora.isEmpty()) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // Verificación de alarmas exactas para Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                context.startActivity(intent)
                return
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("HABIT_NAME", habito.nombre)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habito.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val partesHora = habito.hora.split(":")
        val calendario = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, partesHora[0].toInt())
            set(Calendar.MINUTE, partesHora[1].toInt())
            set(Calendar.SECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendario.timeInMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            // Manejo de error si el permiso fue revocado justo antes
        }
    }

    fun cancelarAlarma(habitoId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habitoId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}