package com.example.gestionhabitos.view.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentAgregarEditarHabitoBinding
import com.example.gestionhabitos.model.entitis.Habito
import com.example.gestionhabitos.viewmodel.HabitoViewModel
import java.util.*

class AgregarEditarHabitoFragment : Fragment() {

    private var _binding: FragmentAgregarEditarHabitoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HabitoViewModel by viewModels()
    private var habitoId: Int = 0 // Para saber si estamos editando

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgregarEditarHabitoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar Categorías
        val categorias = arrayOf("Salud", "Estudio", "Trabajo", "Deporte", "General")
        val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_categoria, categorias)
        binding.actvCategory.setAdapter(adapter)

        // 2. Verificar si es una EDICIÓN
        arguments?.let {
            habitoId = it.getInt("habitoId", 0)
            if (habitoId != 0) {
                binding.tvTitle.text = "Editar Hábito"
                binding.btnSaveHabit.text = "Actualizar Hábito"
                
                // Cargar datos del hábito actual
                viewModel.listaHabitos.observe(viewLifecycleOwner) { habitos ->
                    val habito = habitos.find { h -> h.id == habitoId }
                    habito?.let { h ->
                        binding.etHabitName.setText(h.nombre)
                        binding.actvCategory.setText(h.categoria, false)
                        binding.etHabitTime.setText(h.hora)
                    }
                }
            }
        }

        // 3. Selector de Hora
        binding.etHabitTime.setOnClickListener {
            val calendario = Calendar.getInstance()
            val horaActual = calendario.get(Calendar.HOUR_OF_DAY)
            val minutoActual = calendario.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, hora, minuto ->
                val horaFormateada = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto)
                binding.etHabitTime.setText(horaFormateada)
            }, horaActual, minutoActual, true).show()
        }

        // 4. Lógica de Guardar/Actualizar
        binding.btnSaveHabit.setOnClickListener {
            val nombre = binding.etHabitName.text.toString().trim()
            val categoria = binding.actvCategory.text.toString()
            val hora = binding.etHabitTime.text.toString()

            if (nombre.isNotEmpty()) {
                val habito = Habito(
                    id = habitoId, // Si es 0 crea uno nuevo, si no, actualiza el existente
                    nombre = nombre,
                    categoria = if (categoria.isNotEmpty()) categoria else "General",
                    hora = hora,
                    completado = false // Al editar o crear, lo reseteamos o mantenemos (puedes ajustar esto)
                )

                if (habitoId == 0) viewModel.insertar(habito) else viewModel.actualizarHabito(habito)
                
                Toast.makeText(requireContext(), "Hábito guardado", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                binding.tilName.error = "Escribe un nombre"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}