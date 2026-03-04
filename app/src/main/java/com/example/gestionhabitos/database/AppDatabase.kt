package com.example.gestionhabitos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gestionhabitos.model.dao.*
import com.example.gestionhabitos.model.entitis.*

@Database(
    entities = [Habito::class, RegistroHabito::class, Categoria::class, Usuario::class, Objetivo::class],
    version = 3, // Subimos a 3 para forzar la limpieza tras desinstalar
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitoDao(): HabitoDao
    abstract fun registroHabitoDao(): RegistroHabitoDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun objetivoDao(): ObjetivoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habitflow_db"
                )
                    // Esto permite que la base de datos se reconstruya si cambias algo en las entities
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}