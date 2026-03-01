package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentPerfilBinding
import com.example.gestionhabitos.model.entitis.Usuario
import com.example.gestionhabitos.viewmodel.UsuarioViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class PerfilFragment : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observar datos de Room
        usuarioViewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                binding.tvUserNameProfile.text = it.nombre
                binding.tvUserEmail.text = it.correo
            }
        }

        binding.btnEditProfile.setOnClickListener { mostrarDialogoEdicion() }
    }

    private fun mostrarDialogoEdicion() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_editar_perfil, null)
        val etNombre = view.findViewById<TextInputEditText>(R.id.etNombreDialog)
        val etCorreo = view.findViewById<TextInputEditText>(R.id.etCorreoDialog)

        etNombre.setText(binding.tvUserNameProfile.text)
        etCorreo.setText(binding.tvUserEmail.text)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Editar Perfil")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val correo = etCorreo.text.toString()
                if (nombre.isNotEmpty() && correo.isNotEmpty()) {
                    usuarioViewModel.actualizarUsuario(Usuario(1, nombre, correo, "1234"))
                    Toast.makeText(requireContext(), "Actualizado", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}