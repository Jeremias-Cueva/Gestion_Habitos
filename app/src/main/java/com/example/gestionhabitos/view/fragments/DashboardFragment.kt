package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gestionhabitos.databinding.FragmentDashboardBinding
import com.example.gestionhabitos.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // Inicializamos el ViewModel para acceder a los datos de Room
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar el Saludo Dinámico
        // Aquí podrías obtener el nombre desde una base de datos de Usuario en el futuro
        binding.tvWelcome.text = "¡Hola, Jeremías!"

        // 2. Observar la Frase Motivacional desde el ViewModel
        viewModel.fraseMotivacional.observe(viewLifecycleOwner) { frase ->
            binding.tvQuote.text = "\"$frase\""
        }

        // 3. Observar el Progreso de los Hábitos
        viewModel.porcentajeProgreso.observe(viewLifecycleOwner) { progreso ->
            // El segundo parámetro 'true' hace que la barra se mueva con una animación suave
            binding.progressHabitos.setProgress(progreso, true)
            binding.tvPorcentaje.text = "$progreso% de tus hábitos completados"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}