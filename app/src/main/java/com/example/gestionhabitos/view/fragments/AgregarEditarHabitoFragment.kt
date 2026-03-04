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
import com.example.gestionhabitos.notifications.AlarmHelper
import com.example.gestionhabitos.viewmodel.HabitoViewModel
import com.example.gestionhabitos.viewmodel.UsuarioViewModel
import java.util.Calendar
import java.util.Locale

class AgregarEditarHabitoFragment : Fragment() {

    private var _binding: FragmentAgregarEditarHabitoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HabitoViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
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

        // 1. Configurar Categorías
        val categorias = arrayOf("Salud", "Estudio", "Trabajo", "Deporte", "General")

        // 2. Configurar el Adapter
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_categoria,
            categorias
        )
        binding.actvCategory.setAdapter(adapter)

        // 3. Configurar el Selector de Hora
        binding.etHabitTime.setOnClickListener {
            val calendario = Calendar.getInstance()
            val horaActual = calendario.get(Calendar.HOUR_OF_DAY)
            val minutoActual = calendario.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, hora, minuto ->
                val horaFormateada = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto)
                binding.etHabitTime.setText(horaFormateada)
            }, horaActual, minutoActual, true).show()
        }

        // 4. Lógica del botón Guardar (CORREGIDA)
        binding.btnSaveHabit.setOnClickListener {
            val nombre = binding.etHabitName.text.toString().trim()
            val categoriaSeleccionada = binding.actvCategory.text.toString()
            val horaSeleccionada = binding.etHabitTime.text.toString()

            // Obtenemos el usuario de la sesión actual (ID 1 en Room)
            val usuarioActual = usuarioViewModel.datosUsuario.value

            if (nombre.isNotEmpty() && usuarioActual != null) {
                // Ahora pasamos el usuarioEmail que requiere la entidad Habito
                val nuevoHabito = Habito(
                    nombre = nombre,
                    categoria = if (categoriaSeleccionada.isNotEmpty()) categoriaSeleccionada else "General",
                    hora = horaSeleccionada,
                    usuarioEmail = usuarioActual.email // <--- Vínculo para MockAPI
                )

                viewModel.insertar(nuevoHabito)
                Toast.makeText(requireContext(), "Hábito guardado correctamente", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                if (usuarioActual == null) {
                    Toast.makeText(requireContext(), "Error: No se encontró sesión activa", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Por favor, escribe un nombre", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}