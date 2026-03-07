package com.example.gestionhabitos.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentListaHabitosBinding
import com.example.gestionhabitos.model.entitis.Habito
import com.example.gestionhabitos.view.adapters.HabitoAdapter
import com.example.gestionhabitos.viewmodel.HabitoViewModel
import com.example.gestionhabitos.viewmodel.UsuarioViewModel

class ListaHabitosFragment : Fragment() {

    private var _binding: FragmentListaHabitosBinding? = null
    private val binding get() = _binding!!

    private val habitoViewModel: HabitoViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    private val adapter by lazy {
        HabitoAdapter(
            onHabitChecked = { habito, isChecked ->
                habitoViewModel.actualizarEstadoHabito(habito, isChecked)
            },
            onHabitClick = { habito ->
                // 🔥 RESTAURADO: Navegar a la pantalla de edición al hacer clic
                requireActivity()
                    .findViewById<View>(R.id.nav_host_fragment)
                    .visibility = View.VISIBLE

                val bundle = Bundle().apply {
                    putInt("habitoId", habito.id)
                }
                findNavController()
                    .navigate(R.id.action_listaHabitosFragment_to_agregarEditarHabitoFragment, bundle)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaHabitosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHabitos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHabitos.adapter = adapter

        // SWIPE PARA BORRAR CON CONFIRMACIÓN
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val habito: Habito = adapter.currentList[position]

                AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar hábito")
                    .setMessage("¿Estás seguro de querer borrar '${habito.nombre}'?")
                    .setPositiveButton("Sí") { _, _ ->
                        habitoViewModel.eliminar(habito)
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                        adapter.notifyItemChanged(position)
                    }
                    .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvHabitos)

        binding.fabAddHabit.setOnClickListener {
            requireActivity()
                .findViewById<View>(R.id.nav_host_fragment)
                .visibility = View.VISIBLE

            findNavController()
                .navigate(R.id.action_listaHabitosFragment_to_agregarEditarHabitoFragment)
        }

        usuarioViewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                habitoViewModel.cargarHabitosDeUsuario(it.email)
            }
        }

        habitoViewModel.listaHabitos.observe(viewLifecycleOwner) { habitos ->
            adapter.submitList(habitos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}