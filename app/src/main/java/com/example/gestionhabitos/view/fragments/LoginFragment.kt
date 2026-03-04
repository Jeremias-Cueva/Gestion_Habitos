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

        // --- 1. LÓGICA DE AUTO-LOGIN SEGURA ---
        viewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                // Verificamos que el destino actual sea el Login antes de navegar
                // Esto evita cierres inesperados por navegación duplicada
                if (findNavController().currentDestination?.id == R.id.loginFragment) {
                    findNavController().navigate(R.id.action_loginFragment_to_listaHabitosFragment)
                }
            }
        }

        // --- 2. BOTÓN DE INICIAR SESIÓN ---
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
                Toast.makeText(requireContext(), "Iniciando sesión...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Completa los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // --- 3. BOTÓN PARA IR AL REGISTRO ---
        binding.tvIrARegistro.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.loginFragment) {
                findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
            }
        }

        // --- 4. RESULTADO DEL LOGIN ---
        viewModel.loginResult.observe(viewLifecycleOwner) { esExitoso ->
            when (esExitoso) {
                true -> {
                    Toast.makeText(requireContext(), "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                    if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_listaHabitosFragment)
                    }
                    viewModel.resetLoginResult()
                }
                false -> {
                    Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
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