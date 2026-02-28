package com.example.gestionhabitos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configuración de márgenes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- ESTA ES LA PARTE QUE HACE QUE LOS BOTONES FUNCIONEN ---

        // 1. Obtener el NavHostFragment que contiene tus pantallas
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // 2. Obtener el NavController
        val navController = navHostFragment.navController

        // 3. Obtener la referencia a la barra de menú inferior
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 4. Vincular el menú con el controlador de navegación
        bottomNav.setupWithNavController(navController)
    }
}