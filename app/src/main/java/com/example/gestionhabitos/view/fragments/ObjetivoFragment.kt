package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentObjetivoBinding
import com.example.gestionhabitos.model.entitis.Objetivo
import com.example.gestionhabitos.view.adapters.ObjetivoAdapter
import com.example.gestionhabitos.viewmodel.ObjetivoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ObjetivoFragment : Fragment() {

    private var _binding: FragmentObjetivoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ObjetivoViewModel by viewModels()
    private lateinit var adapter: ObjetivoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObjetivoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.fabAddObjetivo.setOnClickListener {
            mostrarDialogoAgregar()
        }
    }

    private fun setupRecyclerView() {
        adapter = ObjetivoAdapter(emptyList()) { objetivo ->
            mostrarOpcionesObjetivo(objetivo)
        }
        binding.rvObjetivos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ObjetivoFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.listaObjetivos.observe(viewLifecycleOwner) { objetivos ->
            adapter.updateList(objetivos)
        }
    }

    private fun mostrarDialogoAgregar() {
        val dialogView = layoutInflater.inflate(R.layout.fragment_agregar_objetivo, null)
        val etTitulo = dialogView.findViewById<EditText>(R.id.etTituloObjetivo)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDescripcionObjetivo)
        val etMeta = dialogView.findViewById<EditText>(R.id.etMetaValor)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuevo Objetivo")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = etTitulo.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                val meta = etMeta.text.toString().toDoubleOrNull() ?: 0.0

                if (titulo.isNotEmpty() && meta > 0) {
                    viewModel.agregarObjetivo(titulo, desc, meta)
                } else {
                    Toast.makeText(requireContext(), "Datos inválidos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarOpcionesObjetivo(objetivo: Objetivo) {
        val opciones = arrayOf("Actualizar Progreso", "Editar Detalles", "Eliminar")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(objetivo.titulo)
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> mostrarDialogoActualizarProgreso(objetivo)
                    1 -> mostrarDialogoEditar(objetivo)
                    2 -> confirmarEliminacion(objetivo)
                }
            }
            .show()
    }

    private fun mostrarDialogoActualizarProgreso(objetivo: Objetivo) {
        val etProgreso = EditText(requireContext()).apply {
            hint = "Nuevo valor actual"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(objetivo.valorActual.toString())
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Actualizar Progreso")
            .setView(etProgreso)
            .setPositiveButton("Actualizar") { _, _ ->
                val nuevoValor = etProgreso.text.toString().toDoubleOrNull() ?: objetivo.valorActual
                viewModel.actualizarProgreso(objetivo, nuevoValor)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditar(objetivo: Objetivo) {
        val dialogView = layoutInflater.inflate(R.layout.fragment_agregar_objetivo, null)
        val etTitulo = dialogView.findViewById<EditText>(R.id.etTituloObjetivo)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDescripcionObjetivo)
        val etMeta = dialogView.findViewById<EditText>(R.id.etMetaValor)

        etTitulo.setText(objetivo.titulo)
        etDesc.setText(objetivo.descripcion)
        etMeta.setText(objetivo.metaValor.toString())

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Editar Objetivo")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = etTitulo.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                val meta = etMeta.text.toString().toDoubleOrNull() ?: objetivo.metaValor

                if (titulo.isNotEmpty()) {
                    val actualizado = objetivo.copy(
                        titulo = titulo,
                        descripcion = desc,
                        metaValor = meta,
                        completado = objetivo.valorActual >= meta
                    )
                    viewModel.actualizarProgreso(actualizado, objetivo.valorActual) // Reutilizamos actualización
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminacion(objetivo: Objetivo) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¿Eliminar objetivo?")
            .setMessage("¿Estás seguro de que deseas eliminar '${objetivo.titulo}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarObjetivo(objetivo)
                Toast.makeText(requireContext(), "Objetivo eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}