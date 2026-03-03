package com.example.gestionhabitos.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gestionhabitos.databinding.FragmentEstadisticasBinding
import com.example.gestionhabitos.viewmodel.EstadisticasViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class EstadisticasFragment : Fragment() {
    private var _binding: FragmentEstadisticasBinding? = null
    private val binding get() = _binding!!

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

        configurarGraficaBarras()

        // 1. Observar progreso diario (Círculo)
        viewModel.estadisticasProgreso.observe(viewLifecycleOwner) { (completados, total) ->
            if (total > 0) {
                val porcentaje = (completados * 100) / total
                binding.circularProgress.setProgress(porcentaje, true)
                binding.tvPorcentajeGrande.text = "$porcentaje%"
                binding.tvTotalStats.text = "$completados de $total hábitos logrados"

                binding.tvMessage.text = when {
                    porcentaje == 100 -> "¡Increíble! Has cumplido todo hoy. 🎉"
                    porcentaje >= 50 -> "¡Buen trabajo! Vas por buen camino. 💪"
                    else -> "¡Tú puedes! Cada pequeño paso cuenta. ✨"
                }
            }
        }

        // 2. Observar progreso semanal (Gráfica de Barras)
        viewModel.progresoSemanal.observe(viewLifecycleOwner) { progreso ->
            actualizarGraficaBarras(progreso)
        }
    }

    private fun configurarGraficaBarras() {
        binding.barChart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setFitBars(true)
            animateY(1000)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textColor = Color.GRAY
            }

            axisLeft.apply {
                setDrawGridLines(true)
                axisMinimum = 0f
                textColor = Color.GRAY
            }

            axisRight.isEnabled = false
            legend.isEnabled = false
        }
    }

    private fun actualizarGraficaBarras(progreso: List<com.example.gestionhabitos.model.dao.ProgresoDiario>) {
        val entries = progreso.mapIndexed { index, data ->
            BarEntry(index.toFloat(), data.cantidad.toFloat())
        }

        val dataSet = BarDataSet(entries, "Hábitos")
        dataSet.color = Color.parseColor("#6366F1")
        dataSet.valueTextSize = 10f
        dataSet.setDrawValues(true)

        // Formateador para mostrar fechas en el eje X
        binding.barChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < progreso.size) {
                    progreso[index].fecha.substring(5) // Mostrar solo MM-DD
                } else ""
            }
        }

        binding.barChart.data = BarData(dataSet)
        binding.barChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}