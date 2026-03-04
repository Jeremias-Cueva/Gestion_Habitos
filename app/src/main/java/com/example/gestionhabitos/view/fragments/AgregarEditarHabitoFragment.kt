package com.example.gestionhabitos.view.fragments

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
import com.example.gestionhabitos.notifications.AlarmHelper
import com.example.gestionhabitos.viewmodel.HabitoViewModel
import java.util.Calendar
import java.util.Locale

class AgregarEditarHabitoFragment : Fragment() {

    private var _binding: FragmentAgregarEditarHabitoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HabitoViewModel by viewModels()
    private var habitoId: Int = 0
    private lateinit var alarmHelper: AlarmHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgregarEditarHabitoBinding.inflate(inflater, container, false)
        alarmHelper = AlarmHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Aquí defines las opciones que se verán en el menú
        // 1. Configurar Categorías
        val categorias = arrayOf("Salud", "Estudio", "Trabajo", "Deporte", "General")

        // 2. ESTE ES EL ARRAYADAPTER QUE DEBES CORREGIR
        // Cambiamos 'android.R.layout.simple_dropdown_item_1line' por TU nuevo layout
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_categoria, // <--- Usa el archivo que creaste para el color negro
            categorias
        )

        // 3. Se vincula al componente visual
        val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_categoria, categorias)
        binding.actvCategory.setAdapter(adapter)
    //Mira franco Si vale la interfaz

        // 2. Configurar el Selector de Hora
        binding.etHabitTime.setOnClickListener {
            val calendario = Calendar.getInstance()
            val horaActual = calendario.get(Calendar.HOUR_OF_DAY)
            val minutoActual = calendario.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, hora, minuto ->
                // Formatear la hora para que siempre tenga dos dígitos (ej: 08:05)
                val horaFormateada = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto)
                binding.etHabitTime.setText(horaFormateada)
            }, horaActual, minutoActual, true).show()
        }

        // 3. Lógica del botón Guardar
        binding.btnSaveHabit.setOnClickListener {
            val nombre = binding.etHabitName.text.toString().trim()
            val categoriaSeleccionada = binding.actvCategory.text.toString()
            val horaSeleccionada = binding.etHabitTime.text.toString()

            if (nombre.isNotEmpty()) {
                val nuevoHabito = Habito(
                    nombre = nombre,
                    categoria = if (categoriaSeleccionada.isNotEmpty()) categoriaSeleccionada else "General",
                    hora = horaSeleccionada // Guardamos la hora
                )

                viewModel.insertar(nuevoHabito)
                Toast.makeText(requireContext(), "Hábito guardado correctamente", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}