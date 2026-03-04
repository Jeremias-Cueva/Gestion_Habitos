package com.example.gestionhabitos.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.gestionhabitos.model.entitis.Habito
import java.util.*

class AlarmHelper(private val context: Context) {

    fun programarAlarma(habito: Habito) {
        if (habito.hora.isEmpty()) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("HABIT_NAME", habito.nombre)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habito.id, // Usamos el ID del hábito como requestCode único
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val partesHora = habito.hora.split(":")
        val calendario = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, partesHora[0].toInt())
            set(Calendar.MINUTE, partesHora[1].toInt())
            set(Calendar.SECOND, 0)

            // Si la hora ya pasó hoy, programarla para mañana
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendario.timeInMillis,
            pendingIntent
        )
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