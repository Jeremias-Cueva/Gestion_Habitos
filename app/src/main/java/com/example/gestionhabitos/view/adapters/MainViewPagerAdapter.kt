package com.example.gestionhabitos.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gestionhabitos.view.fragments.DashboardFragment
import com.example.gestionhabitos.view.fragments.EstadisticasFragment
import com.example.gestionhabitos.view.fragments.ListaHabitosFragment
import com.example.gestionhabitos.view.fragments.ObjetivoFragment
import com.example.gestionhabitos.view.fragments.PerfilFragment

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    // 🛡️ CORRECCIÓN: Ahora son 5 pantallas para HabitFlow
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DashboardFragment()
            1 -> ListaHabitosFragment()
            2 -> EstadisticasFragment()
            3 -> ObjetivoFragment() // 👈 Nueva posición para Objetivos
            4 -> PerfilFragment()    // 👈 El perfil ahora es la última pestaña

            else -> DashboardFragment()
        }
    }
}