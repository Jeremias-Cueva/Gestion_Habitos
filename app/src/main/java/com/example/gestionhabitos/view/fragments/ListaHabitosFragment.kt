package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentListaHabitosBinding
import com.example.gestionhabitos.view.adapters.HabitoAdapter
import com.example.gestionhabitos.viewmodel.HabitoViewModel
import com.example.gestionhabitos.viewmodel.UsuarioViewModel

class ListaHabitosFragment : Fragment() {

    private var _binding: FragmentListaHabitosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HabitoViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaHabitosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CONFIGURACIÓN DEL ADAPTADOR
        val adapter = HabitoAdapter(
            listaHabitos = emptyList(),
            onHabitChecked = { habito, isChecked ->
                viewModel.actualizarEstadoHabito(habito, isChecked)
            },
            onHabitClick = { habito ->
                navegarSeguro(habito.id)
            }
        )

        binding.rvHabitos.adapter = adapter
        binding.rvHabitos.layoutManager = LinearLayoutManager(requireContext())

        // 1. VÍNCULO CRÍTICO: VALIDAR SESIÓN Y FILTRAR
        usuarioViewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                binding.rvHabitos.visibility = View.VISIBLE
                viewModel.cargarHabitosDeUsuario(usuario.email)
            }
        }

        // 2. OBSERVAR LA LISTA FILTRADA
        viewModel.listaHabitos.observe(viewLifecycleOwner) { habitos ->
            adapter.updateList(habitos ?: emptyList())
        }

        // Lógica para eliminar con Swiped
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val habitoAEliminar = adapter.listaHabitos[posicion]

                AlertDialog.Builder(requireContext())
                    .setTitle("¿Eliminar hábito?")
                    .setMessage("¿Estás seguro de que deseas eliminar '${habitoAEliminar.nombre}'?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        viewModel.eliminar(habitoAEliminar)
                        Toast.makeText(requireContext(), "Hábito eliminado", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancelar") { _, _ ->
                        adapter.notifyItemChanged(posicion)
                    }
                    .setCancelable(false)
                    .show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvHabitos)

        // BOTÓN AGREGAR
        binding.fabAddHabit.setOnClickListener {
            navegarSeguro()
        }
    }

    // FUNCIÓN DE NAVEGACIÓN INFALIBLE
    private fun navegarSeguro(habitoId: Int? = null) {
        try {
            // Buscamos el NavController directamente desde la Activity
            // Esto "despierta" la navegación principal de la tesis
            val navController = androidx.navigation.Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

            // Hacemos que el contenedor sea visible ANTES de navegar
            requireActivity().findViewById<View>(R.id.nav_host_fragment).visibility = View.VISIBLE

            val bundle = Bundle()
            if (habitoId != null) bundle.putInt("habitoId", habitoId)

            navController.navigate(R.id.action_listaHabitosFragment_to_agregarEditarHabitoFragment, bundle)

        } catch (e: Exception) {
            Log.e("ERROR_NAVEGACION", "Fallo: ${e.message}")
            // Si el método anterior falla, intentamos el tradicional pero forzado
            try {
                findNavController().navigate(R.id.action_listaHabitosFragment_to_agregarEditarHabitoFragment)
            } catch (e2: Exception) {
                Toast.makeText(requireContext(), "Error de ruta: ${e2.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}