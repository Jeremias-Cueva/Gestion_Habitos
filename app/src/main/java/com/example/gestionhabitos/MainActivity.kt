package com.example.gestionhabitos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.ActivityMainBinding
import com.example.gestionhabitos.view.adapters.MainViewPagerAdapter
import com.example.gestionhabitos.viewmodel.UsuarioViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notificaciones activadas", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pedirPermisoNotificaciones()

        usuarioViewModel.datosUsuario.observe(this) { usuario ->
            if (usuario != null) {
                mostrarPantallaPrincipal()
            } else {
                mostrarPantallaLogin()
            }
        }
    }

    private fun mostrarPantallaPrincipal() {
        binding.viewPager.visibility = View.VISIBLE
        binding.bottomNavigation.visibility = View.VISIBLE

        // 🛡️ CORRECCIÓN: Usamos el nombre exacto del XML a través de binding
        // Cambiamos GONE por INVISIBLE para que el NavController no se destruya
        binding.navHostFragment.visibility = View.INVISIBLE

        if (binding.viewPager.adapter == null) {
            val adapter = MainViewPagerAdapter(this)
            binding.viewPager.adapter = adapter

            binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNavigation.menu.getItem(position).isChecked = true
                }
            })

            binding.bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.dashboardFragment -> binding.viewPager.currentItem = 0
                    R.id.listaHabitosFragment -> binding.viewPager.currentItem = 1
                    R.id.estadisticasFragment -> binding.viewPager.currentItem = 2
                    R.id.perfilFragment -> binding.viewPager.currentItem = 3
                }
                true
            }
        }
    }

    private fun mostrarPantallaLogin() {
        binding.viewPager.visibility = View.GONE
        binding.bottomNavigation.visibility = View.GONE
        // Aseguramos que el contenedor de navegación sea visible para el Login
        binding.navHostFragment.visibility = View.VISIBLE
    }

    private fun pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}