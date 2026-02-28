package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

        binding.btnSaveHabit.setOnClickListener {

            val nombre = binding.tilHabitName.editText?.text.toString().trim()

            if (nombre.isNotEmpty()) {

                val nuevoHabito = Habito(
                    nombre = nombre,
                    categoria = "General"
                )

                viewModel.insertar(nuevoHabito)

                Toast.makeText(context, "Hábito guardado correctamente", Toast.LENGTH_SHORT).show()

                findNavController().popBackStack()

            } else {
                binding.tilHabitName.error = "Por favor, escribe un nombre"
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}