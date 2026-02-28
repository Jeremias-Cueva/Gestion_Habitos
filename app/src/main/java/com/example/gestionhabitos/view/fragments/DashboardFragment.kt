package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gestionhabitos.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // Listas para saludos y frases aleatorias
    private val saludos = listOf("¡Hola!", "¡Buen día!", "¡Qué gusto verte!", "¡A darle!")
    private val frases = listOf(
        "La disciplina es el puente entre las metas y los logros.",
        "Tus hábitos determinan tu futuro.",
        "No te detengas hasta que te sientas orgulloso.",
        "Pequeños pasos conducen a grandes resultados."
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // Esto hace que el saludo y la frase cambien solos
        binding.tvWelcome.text = saludos.random()
        binding.tvQuote.text = "\"${frases.random()}\""

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}