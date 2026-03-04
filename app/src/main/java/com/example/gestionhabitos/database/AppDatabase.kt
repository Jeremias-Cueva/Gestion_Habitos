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
    version = 2, // Subimos la versión a 2 para reflejar el cambio en la tabla
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

        // Definimos la migración para no perder los datos actuales
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE habitos ADD COLUMN hora TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habitflow_db"
                )
                .addMigrations(MIGRATION_1_2) // Añadimos la migración
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // CORRECCIÓN: Usa 'email' y 'password' para que coincida con tu Entity Usuario
                            db.execSQL("INSERT INTO usuarios (id, nombre, email, password) VALUES (1, 'Jeremías Santiago', 'correo@ejemplo.com', '1234')")
                        }
                    })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}