package com.example.gestionhabitos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.ActivityMainBinding
import com.example.gestionhabitos.view.adapters.MainViewPagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        setupViewPager()
        pedirPermisoNotificaciones()
    }

    private fun setupViewPager() {
        val adapter = MainViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // 1. Al deslizar el ViewPager, actualizar el BottomNavigationView
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigation.menu.getItem(position).isChecked = true
            }
        })

        // 2. Al tocar el BottomNavigationView, deslizar el ViewPager
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardFragment -> binding.viewPager.currentItem = 0
                R.id.listaHabitosFragment -> binding.viewPager.currentItem = 1
                R.id.estadisticasFragment -> binding.viewPager.currentItem = 2
                R.id.perfilFragment -> binding.viewPager.currentItem = 3
            }
            true
        }
        
        // Evitar que el ViewPager se deslice cuando estamos en otras pantallas (opcional)
        // binding.viewPager.isUserInputEnabled = true 
    }

    private fun pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}