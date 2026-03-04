package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.FragmentLoginBinding
import com.example.gestionhabitos.viewmodel.UsuarioViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UsuarioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- LÓGICA DE AUTO-LOGIN (DETECTAR SESIÓN) ---
        // Si Room ya tiene un usuario con ID 1, saltamos el login de inmediato
        viewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                findNavController().navigate(R.id.action_loginFragment_to_listaHabitosFragment)
            }
        }
        // ----------------------------------------------

        // Botón para ir a la pantalla de Registro
        binding.tvIrARegistro.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
        }

        // Botón de Inicio de Sesión Manual
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
                Toast.makeText(requireContext(), "Validando credenciales...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Observador para el resultado del Login manual con MockAPI
        viewModel.loginResult.observe(viewLifecycleOwner) { esExitoso ->
            when (esExitoso) {
                true -> {
                    // No hace falta navigate aquí porque al guardar en Room,
                    // el observador de datosUsuario de arriba se disparará solo.
                    viewModel.resetLoginResult()
                }
                false -> {
                    Toast.makeText(requireContext(), "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    viewModel.resetLoginResult()
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}