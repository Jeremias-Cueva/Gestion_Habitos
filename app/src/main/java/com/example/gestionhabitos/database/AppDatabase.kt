package com.example.gestionhabitos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
// Imports manuales exactos para que Room encuentre tus clases
import com.example.gestionhabitos.model.entitis.Habito
import com.example.gestionhabitos.model.entitis.RegistroHabito
import com.example.gestionhabitos.model.entitis.Categoria
import com.example.gestionhabitos.model.entitis.Usuario
import com.example.gestionhabitos.model.entitis.Objetivo
import com.example.gestionhabitos.model.dao.HabitoDao
import com.example.gestionhabitos.model.dao.RegistroHabitoDao
import com.example.gestionhabitos.model.dao.CategoriaDao
import com.example.gestionhabitos.model.dao.UsuarioDao
import com.example.gestionhabitos.model.dao.ObjetivoDao

@Database(
    entities = [ Habito::class, RegistroHabito::class, Categoria::class, Usuario::class, Objetivo::class],
    version = 12, // Versión actualizada para tu tesis de HabitFlow
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Vinculación con los DAOs
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
                    "habitflow_final_v12" // Nueva base de datos limpia
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}