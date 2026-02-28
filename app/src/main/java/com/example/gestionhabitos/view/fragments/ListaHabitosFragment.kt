package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentListaHabitosBinding
import com.example.gestionhabitos.view.adapters.HabitoAdapter
import com.example.gestionhabitos.viewmodel.HabitoViewModel

class ListaHabitosFragment : Fragment() {

    private var _binding: FragmentListaHabitosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HabitoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaHabitosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HabitoAdapter(emptyList()) { habito, isChecked ->
            viewModel.actualizarEstadoHabito(habito, isChecked)
        }

        binding.rvHabitos.adapter = adapter
        binding.rvHabitos.layoutManager = LinearLayoutManager(requireContext())

        viewModel.listaHabitos.observe(viewLifecycleOwner) { habitos ->
            adapter.updateList(habitos)
        }

        // --- LÓGICA DE ELIMINACIÓN POR DESLIZAMIENTO (SWIPE) ---
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val habitoAEliminar = adapter.listaHabitos[posicion] // Acceso directo a la lista

                viewModel.eliminar(habitoAEliminar) // Ejecuta la eliminación en Room
                Toast.makeText(requireContext(), "Hábito eliminado", Toast.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvHabitos)
        // ------------------------------------------------------------------

        binding.fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_listaHabitosFragment_to_agregarEditarHabitoFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}