package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gestionhabitos.MainActivity
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentAgregarObjetivoBinding
import com.example.gestionhabitos.viewmodel.ObjetivoViewModel

class AgregarObjetivoFragment : Fragment(R.layout.fragment_agregar_objetivo) {

    private val viewModel: ObjetivoViewModel by viewModels()
    private var _binding: FragmentAgregarObjetivoBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAgregarObjetivoBinding.bind(view)

        binding.btnGuardarObjetivo.setOnClickListener {
            val titulo = binding.etTituloObjetivo.text.toString().trim()
            val descripcion = binding.etDescripcionObjetivo.text.toString().trim()
            val meta = binding.etMetaValor.text.toString().toDoubleOrNull() ?: 0.0

            if (titulo.isNotEmpty() && meta > 0) {
                // 🛡️ CORRECCIÓN: Llamamos a la función que SÍ existe en el ViewModel
                viewModel.agregarObjetivo(titulo, descripcion, meta)
                
                Toast.makeText(requireContext(), "Objetivo guardado correctamente", Toast.LENGTH_SHORT).show()

                // Restaurar visibilidad si es necesario (según tu lógica de MainActivity)
                // (activity as? MainActivity)?.mostrarPantallaPrincipal() 

                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Por favor, ingresa un título y una meta válida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}