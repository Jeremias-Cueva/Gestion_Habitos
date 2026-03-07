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
    
    private var habitoId: Int = 0
    private var datosCargados = false

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

        // 1. Cargar sesión y disparar carga de hábitos
        usuarioViewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            usuarioLogueado = usuario
            usuario?.let {
                viewModel.cargarHabitosDeUsuario(it.email)
            }
        }

        // 2. Adaptador de categorías
        val categorias = arrayOf("Salud", "Estudio", "Trabajo", "Deporte", "General")
        val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_categoria, categorias)
        binding.actvCategory.setAdapter(adapter)

        // 3. RECUPERAR DATOS SI ES EDICIÓN
        arguments?.let { bundle ->
            habitoId = bundle.getInt("habitoId", 0)
            if (habitoId != 0) {
                binding.tvTitle.text = "Editar Hábito"
                binding.btnSaveHabit.text = "Actualizar Hábito"
                
                // Observar cambios en la lista para rellenar los campos al editar
                viewModel.listaHabitos.observe(viewLifecycleOwner) { habitos ->
                    if (!datosCargados && habitos.isNotEmpty()) {
                        val habito = habitos.find { h -> h.id == habitoId }
                        habito?.let { h ->
                            binding.etHabitName.setText(h.nombre)
                            
                            val nombreCat = when(h.categoriaId) {
                                1 -> "General"
                                2 -> "Salud"
                                3 -> "Estudio"
                                4 -> "Trabajo"
                                5 -> "Deporte"
                                else -> "General"
                            }
                            binding.actvCategory.setText(nombreCat, false)
                            binding.etHabitTime.setText(h.hora)
                            datosCargados = true // Evita que se sobreescriba si el usuario está escribiendo
                        }
                    }
                }
            }
        }

        // 4. Selector de Hora
        binding.etHabitTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, h, m ->
                binding.etHabitTime.setText(String.format(Locale.getDefault(), "%02d:%02d", h, m))
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        // 5. Botón Guardar / Actualizar
        binding.btnSaveHabit.setOnClickListener {
            val nombre = binding.etHabitName.text.toString().trim()
            val categoriaSeleccionada = binding.actvCategory.text.toString()
            val hora = binding.etHabitTime.text.toString()
            val user = usuarioLogueado

            if (nombre.isNotEmpty() && user != null) {
                val emailLimpio = user.email.trim().lowercase()

                val idCategoria = when (categoriaSeleccionada) {
                    "General" -> 1
                    "Salud" -> 2
                    "Estudio" -> 3
                    "Trabajo" -> 4
                    "Deporte" -> 5
                    else -> 1
                }

                val habitoAGuardar = Habito(
                    id = habitoId,
                    nombre = nombre,
                    categoriaId = idCategoria,
                    hora = hora,
                    usuarioEmail = emailLimpio,
                    completado = false,
                    sincronizado = false
                )

                if (habitoId == 0) {
                    viewModel.insertar(habitoAGuardar)
                    Toast.makeText(requireContext(), "Hábito creado", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.actualizar(habitoAGuardar)
                    Toast.makeText(requireContext(), "Hábito actualizado", Toast.LENGTH_SHORT).show()
                }

                if (hora.isNotEmpty()) {
                    alarmHelper.programarAlarma(habitoAGuardar)
                }

                findNavController().popBackStack()

            } else {
                Toast.makeText(requireContext(), "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}