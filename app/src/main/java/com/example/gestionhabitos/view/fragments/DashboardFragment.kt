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

    // Inicializamos los ViewModels
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

        // 1. Configurar el Saludo Dinámico desde la Base de Datos usando strings.xml
        usuarioViewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            val nombre = usuario?.nombre ?: getString(R.string.default_user_name)
            binding.tvWelcome.text = getString(R.string.welcome_message, nombre)
        }

        // 2. Observar la Frase Motivacional desde el ViewModel usando strings.xml
        dashboardViewModel.fraseMotivacional.observe(viewLifecycleOwner) { frase ->
            binding.tvQuote.text = getString(R.string.quote_format, frase)
        }

        // 3. Observar el Progreso de los Hábitos usando strings.xml
        dashboardViewModel.porcentajeProgreso.observe(viewLifecycleOwner) { progreso ->
            // El segundo parámetro 'true' hace que la barra se mueva con una animación suave
            binding.progressHabitos.setProgress(progreso, true)
            binding.tvPorcentaje.text = getString(R.string.progress_status, progreso)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}