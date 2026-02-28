package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentListaHabitosBinding
import com.example.gestionhabitos.view.adapters.HabitoAdapter
import com.example.gestionhabitos.viewmodel.HabitoViewModel

class ListaHabitosFragment : Fragment() {

    private var _binding: FragmentListaHabitosBinding? = null
    private val binding get() = _binding!!

    // Inicializamos el ViewModel usando el delegado de la biblioteca ktx
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

        // 1. Configuramos el adaptador con la lógica para actualizar la DB al marcar el check
        val adapter = HabitoAdapter(emptyList()) { habito, isChecked ->
            // Creamos una copia del hábito con el nuevo estado del Checkbox
            val habitoActualizado = habito.copy(completado = isChecked)
            // Mandamos la actualización a Room a través del ViewModel
            viewModel.actualizar(habitoActualizado)
        }

        // 2. Vinculamos el adaptador y el LayoutManager al RecyclerView
        binding.rvHabitos.adapter = adapter
        binding.rvHabitos.layoutManager = LinearLayoutManager(requireContext())

        // 3. Observamos los cambios en la base de datos para refrescar la lista automáticamente
        viewModel.listaHabitos.observe(viewLifecycleOwner) { habitos ->
            adapter.updateList(habitos)
        }

        // 4. Configuración del botón flotante con la acción de navegación
        binding.fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_listaHabitosFragment_to_agregarEditarHabitoFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}