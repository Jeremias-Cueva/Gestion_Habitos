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

        // 2. Verificar si es una EDICIÓN
        arguments?.let {
            habitoId = it.getInt("habitoId", 0)
            if (habitoId != 0) {
                binding.tvTitle.text = "Editar Hábito"
                binding.btnSaveHabit.text = "Actualizar Hábito"
                
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

        // 4. Lógica de Guardar/Actualizar + Programar Alarma
        binding.btnSaveHabit.setOnClickListener {
            val nombre = binding.etHabitName.text.toString().trim()
            val categoria = binding.actvCategory.text.toString()
            val hora = binding.etHabitTime.text.toString()

            if (nombre.isNotEmpty()) {
                val habito = Habito(
                    id = habitoId,
                    nombre = nombre,
                    categoria = if (categoria.isNotEmpty()) categoria else "General",
                    hora = hora,
                    completado = false
                )

                if (habitoId == 0) {
                    viewModel.insertar(habito)
                } else {
                    viewModel.actualizarHabito(habito)
                }
                
                // --- PROGRAMAR NOTIFICACIÓN ---
                if (hora.isNotEmpty()) {
                    // Si el ID es 0 (nuevo), necesitamos el ID real generado por Room.
                    // Para simplificar, si es nuevo, Room lo inserta y el ViewModel lo gestiona.
                    // Pero para programar la alarma con el ID correcto, observamos el último cambio.
                    // NOTA: Para un flujo perfecto, lo ideal es obtener el ID retornado por la base de datos.
                    alarmHelper.programarAlarma(habito) 
                }

<<<<<<< HEAD
=======
                Toast.makeText(requireContext(), "Hábito guardado y recordatorio programado", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                binding.tilName.error = "Escribe un nombre"
            }
        }
>>>>>>> bc6b22531e1532a6ba40b07aa945fb89a267c032
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}