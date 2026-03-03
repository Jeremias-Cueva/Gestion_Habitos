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
import com.example.gestionhabitos.viewmodel.HabitoViewModel

class AgregarEditarHabitoFragment : Fragment() {

    private var _binding: FragmentAgregarEditarHabitoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HabitoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgregarEditarHabitoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar las opciones con un layout personalizado para el color
        val categorias = arrayOf("Salud", "Estudio", "Trabajo", "Deporte", "General")

        // Usamos nuestro layout personalizado item_dropdown_categoria para forzar el fondo blanco y texto negro
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_categoria,
            categorias
        )
        binding.actvCategory.setAdapter(adapter)

        // 2. Lógica del botón Guardar
        binding.btnSaveHabit.setOnClickListener {
            val nombre = binding.etHabitName.text.toString().trim()
            val categoriaSeleccionada = binding.actvCategory.text.toString()

            if (nombre.isNotEmpty()) {
                val nuevoHabito = Habito(
                    nombre = nombre,
                    categoria = if (categoriaSeleccionada.isNotEmpty()) categoriaSeleccionada else "General"
                )

                viewModel.insertar(nuevoHabito)
                Toast.makeText(requireContext(), "Hábito guardado correctamente", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()

            } else {
                binding.tilName.error = "Por favor, escribe un nombre"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}