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
import com.example.gestionhabitos.model.entitis.Usuario
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
    private var usuarioLogueado: Usuario? = null
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

        usuarioViewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            usuarioLogueado = usuario
        }

        // 1. Configurar el adaptador de categorías
        val categorias = arrayOf("Salud", "Estudio", "Trabajo", "Deporte", "General")
        val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_categoria, categorias)
        binding.actvCategory.setAdapter(adapter)

        // 2. Selector de Hora
        binding.etHabitTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, h, m ->
                binding.etHabitTime.setText(String.format(Locale.getDefault(), "%02d:%02d", h, m))
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        // 3. Botón Guardar
        binding.btnSaveHabit.setOnClickListener {
            val nombre = binding.etHabitName.text.toString().trim()
            val categoriaSeleccionada = binding.actvCategory.text.toString()
            val hora = binding.etHabitTime.text.toString()
            val user = usuarioLogueado

            if (nombre.isNotEmpty() && user != null) {
                val emailLimpio = user.email.trim().lowercase()

                // 🔥 SOLUCIÓN AL "GENERAL": Convertimos el texto a ID real
                val idCategoria = when (categoriaSeleccionada) {
                    "General" -> 1
                    "Salud" -> 2
                    "Estudio" -> 3
                    "Trabajo" -> 4
                    "Deporte" -> 5
                    else -> 6 // "General" o cualquier otro
                }

                // Creamos el objeto Habito con los datos reales
                val nuevoHabito = Habito(
                    nombre = nombre,
                    categoriaId = idCategoria, // ✅ Ahora es dinámico
                    hora = hora,
                    usuarioEmail = emailLimpio,
                    completado = false,
                    sincronizado = false
                )

                // Guardamos usando la nueva lógica del ViewModel/Repository
                viewModel.insertar(nuevoHabito)

                // Programar alarma
                if (hora.isNotEmpty()) {
                    alarmHelper.programarAlarma(nuevoHabito)
                }

                Toast.makeText(requireContext(), "Hábito guardado", Toast.LENGTH_SHORT).show()

                // Limpieza de UI y navegación
                requireActivity().findViewById<View>(R.id.nav_host_fragment).visibility = View.INVISIBLE
                findNavController().popBackStack()

            } else {
                val mensaje = if (user == null) "Cargando sesión..." else "Escribe un nombre"
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}