package com.example.gestionhabitos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gestionhabitos.model.dao.*
import com.example.gestionhabitos.model.entitis.*

@Database(
    entities = [Habito::class, RegistroHabito::class, Categoria::class, Usuario::class, Objetivo::class],
    version = 1,
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
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Usuario inicial por defecto
                        db.execSQL("INSERT INTO usuarios (id, nombre, correo, contrasena) VALUES (1, 'Jeremías Santiago', 'correo@ejemplo.com', '1234')")
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}