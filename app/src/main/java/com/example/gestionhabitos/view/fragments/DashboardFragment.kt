package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentDashboardBinding
import com.example.gestionhabitos.viewmodel.DashboardViewModel
import com.example.gestionhabitos.viewmodel.UsuarioViewModel

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar el Saludo y Cargar Datos Filtrados
        usuarioViewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                // --- CAMBIO CRÍTICO AQUÍ ---
                // Le pasamos el email al DashboardViewModel para que filtre los hábitos
                dashboardViewModel.cargarDatosUsuario(usuario.email)

                val primerNombre = usuario.nombre.split(" ")[0]
                binding.tvWelcome.text = getString(R.string.welcome_message, primerNombre)
            } else {
                binding.tvWelcome.text = getString(R.string.welcome_message, "Usuario")
            }
        }

        // 2. Frase Motivacional
        dashboardViewModel.fraseMotivacional.observe(viewLifecycleOwner) { frase ->
            binding.tvQuote.text = if (!frase.isNullOrEmpty()) {
                getString(R.string.quote_format, frase)
            } else {
                "Cargando frase..."
            }
        }

        // 3. Progreso Diario (Sincronizado con el usuario actual)
        dashboardViewModel.porcentajeProgreso.observe(viewLifecycleOwner) { progreso ->
            // Usamos el progreso filtrado por el email que pasamos en el punto 1
            binding.progressHabitos.setProgress(progreso ?: 0, true)
            binding.tvPorcentaje.text = getString(R.string.progress_status, progreso ?: 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}