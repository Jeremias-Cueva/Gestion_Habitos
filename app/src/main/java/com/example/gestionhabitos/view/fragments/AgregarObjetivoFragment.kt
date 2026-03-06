package com.example.gestionhabitos.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gestionhabitos.MainActivity // 👈 Asegúrate de que este import exista
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentAgregarObjetivoBinding
import com.example.gestionhabitos.model.entitis.Objetivo
import com.example.gestionhabitos.viewmodel.ObjetivoViewModel

class AgregarObjetivoFragment : Fragment(R.layout.fragment_agregar_objetivo) {

    private val viewModel: ObjetivoViewModel by viewModels()
    private var _binding: FragmentAgregarObjetivoBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAgregarObjetivoBinding.bind(view)

        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_email", null)

        binding.btnGuardarObjetivo.setOnClickListener {
            val titulo = binding.etTituloObjetivo.text.toString().trim()
            val descripcion = binding.etDescripcionObjetivo?.text?.toString()?.trim() ?: ""
            val meta = binding.etMetaValor.text.toString().toDoubleOrNull() ?: 0.0

            if (userEmail == null) {
                Toast.makeText(requireContext(), "Error: Sesión no encontrada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (titulo.isNotEmpty() && meta > 0) {
                val nuevo = Objetivo(
                    id = 0,
                    titulo = titulo,
                    descripcion = descripcion,
                    metaValor = meta,
                    valorActual = 0.0,
                    completado = false,
                    usuarioEmail = userEmail,
                    sincronizado = false
                )

                viewModel.insertarObjetivo(nuevo)
                Toast.makeText(requireContext(), "Objetivo creado con éxito", Toast.LENGTH_SHORT).show()

                // 🛡️ CORRECCIÓN: Devolvemos la visibilidad al ViewPager antes de salir
                (activity as? MainActivity)?.setNavegacionPrincipal(true)

                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}