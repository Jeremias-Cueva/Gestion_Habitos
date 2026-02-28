package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gestionhabitos.databinding.FragmentEstadisticasBinding
import com.example.gestionhabitos.viewmodel.EstadisticasViewModel

class EstadisticasFragment : Fragment() {
    private var _binding: FragmentEstadisticasBinding? = null
    private val binding get() = _binding!!

    // Inicializamos el ViewModel
    private val viewModel: EstadisticasViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observamos los datos procesados
        viewModel.estadisticasProgreso.observe(viewLifecycleOwner) { (completados, total) ->
            if (total > 0) {
                val porcentaje = (completados * 100) / total

                // Actualizamos el círculo y los textos
                binding.circularProgress.setProgress(porcentaje, true)
                binding.tvPorcentajeGrande.text = "$porcentaje%"
                binding.tvTotalStats.text = "$completados de $total hábitos logrados"

                // Mensaje dinámico según el desempeño
                binding.tvMessage.text = when {
                    porcentaje == 100 -> "¡Increíble! Has cumplido todo hoy. 🎉"
                    porcentaje >= 50 -> "¡Buen trabajo, Jeremías! Vas por buen camino. 💪"
                    else -> "¡Tú puedes! Cada pequeño paso cuenta. ✨"
                }
            } else {
                binding.tvTotalStats.text = "Aún no tienes hábitos registrados."
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}