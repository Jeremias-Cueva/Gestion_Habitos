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

        // 1. Aquí defines las opciones que se verán en el menú
        val categorias = arrayOf("Salud", "Estudio", "Trabajo", "Deporte", "General")

        // 2. ESTE ES EL ARRAYADAPTER QUE DEBES CORREGIR
        // Cambiamos 'android.R.layout.simple_dropdown_item_1line' por TU nuevo layout
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_categoria, // <--- Usa el archivo que creaste para el color negro
            categorias
        )

        // 3. Se vincula al componente visual
        binding.actvCategory.setAdapter(adapter)
    //Mira franco Si vale la interfaz

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}