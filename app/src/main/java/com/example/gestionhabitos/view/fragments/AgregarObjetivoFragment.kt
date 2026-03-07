package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentAgregarObjetivoBinding

/**
 * Nota: Este fragmento es opcional ya que actualmente se está usando 
 * un diálogo en ObjetivoFragment para agregar objetivos. 
 * Se mantiene corregido para evitar errores de compilación.
 */
class AgregarObjetivoFragment : Fragment(R.layout.fragment_agregar_objetivo) {

    private var _binding: FragmentAgregarObjetivoBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAgregarObjetivoBinding.bind(view)
        
        // El botón btnGuardarObjetivo fue retirado del XML para usar el botón del diálogo.
        // Si en el futuro deseas usar este fragmento como pantalla completa, 
        // deberás añadir un botón al layout y su lógica aquí.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}