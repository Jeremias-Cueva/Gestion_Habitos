package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gestionhabitos.databinding.FragmentPerfilBinding
import com.example.gestionhabitos.model.entitis.Usuario
import com.example.gestionhabitos.viewmodel.UsuarioViewModel
import com.example.gestionhabitos.R
import com.google.android.material.textfield.TextInputEditText
class PerfilFragment : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    // Inicializamos el ViewModel
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Observar los datos de Room para actualizar la UI automáticamente
        usuarioViewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                binding.tvUserNameProfile.text = it.nombre
                binding.tvUserEmail.text = it.correo
            }
        }

        // 2. Configurar el botón de edición con un AlertDialog
        binding.btnEditProfile.setOnClickListener {
            mostrarDialogoEdicion()
        }
    }

    private fun mostrarDialogoEdicion() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_editar_perfil, null)
        val etNombre = dialogView.findViewById<TextInputEditText>(R.id.etNombreDialog)
        val etCorreo = dialogView.findViewById<TextInputEditText>(R.id.etCorreoDialog)

        // Llenamos con los datos actuales
        etNombre.setText(binding.tvUserNameProfile.text)
        etCorreo.setText(binding.tvUserEmail.text)

        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Editar Perfil")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                // Lógica de guardado que ya tienes...
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}