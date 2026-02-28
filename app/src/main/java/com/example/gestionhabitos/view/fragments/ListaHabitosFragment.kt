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

        // El ID correcto es rvHabitos según tu XML
        val adapter = HabitoAdapter(emptyList())
        binding.rvHabitos.adapter = adapter
        binding.rvHabitos.layoutManager = LinearLayoutManager(requireContext())

        // Asegúrate de que en HabitoViewModel la variable se llame listaHabitos
        viewModel.listaHabitos.observe(viewLifecycleOwner) { habitos ->
            adapter.updateList(habitos)
        }

        binding.fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_listaHabitosFragment_to_agregarEditarHabitoFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}