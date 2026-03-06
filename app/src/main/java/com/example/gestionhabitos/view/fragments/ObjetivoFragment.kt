package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionhabitos.MainActivity // 👈 IMPORTANTE: Verifica que esta ruta sea correcta
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentObjetivoBinding
import com.example.gestionhabitos.view.adapters.ObjetivoAdapter
import com.example.gestionhabitos.viewmodel.ObjetivoViewModel
import com.example.gestionhabitos.viewmodel.UsuarioViewModel

class ObjetivoFragment : Fragment() {
    private var _binding: FragmentObjetivoBinding? = null
    private val binding get() = _binding!!

    private val objetivoViewModel: ObjetivoViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private lateinit var objetivoAdapter: ObjetivoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentObjetivoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🛡️ SOLUCIÓN: Ahora la función está definida abajo
        configurarRecyclerView()

        usuarioViewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                objetivoViewModel.obtenerObjetivos(it.email).observe(viewLifecycleOwner) { lista ->
                    objetivoAdapter.submitList(lista)
                }
            }
        }

        binding.fabAgregarObjetivo.setOnClickListener {
            // 🛡️ SOLUCIÓN: Casteo correcto a MainActivity para usar la visibilidad
            (activity as? MainActivity)?.setNavegacionPrincipal(false)
            findNavController().navigate(R.id.action_objetivoFragment_to_agregarObjetivoFragment)
        }
    }

    // 🛡️ SOLUCIÓN: Agregamos la función que faltaba
    private fun configurarRecyclerView() {
        objetivoAdapter = ObjetivoAdapter(
            onEditClick = { obj -> /* Lógica editar */ },
            onDeleteClick = { obj -> objetivoViewModel.eliminarObjetivo(obj) }
        )
        binding.rvObjetivos.apply {
            adapter = objetivoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}