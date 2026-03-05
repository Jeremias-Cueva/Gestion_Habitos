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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Auto-Login (Solo si la app abre y ya hay alguien)
        viewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null && viewModel.loginResult.value == null) {
                irALista()
            }
        }

        // 2. Botón Login
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.login(email, pass)
                Toast.makeText(requireContext(), "Iniciando...", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Botón Registro
        binding.tvIrARegistro.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
        }

        // 4. Botón Olvidé mi contraseña
        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recuperarPasswordFragment)
        }

        // 5. Resultado del Login (Manual)
        viewModel.loginResult.observe(viewLifecycleOwner) { esExitoso ->
            if (esExitoso == true) {
                irALista()
                viewModel.resetLoginResult()
            } else if (esExitoso == false) {
                Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                viewModel.resetLoginResult()
            }
        }
    }

    private fun irALista() {
        if (findNavController().currentDestination?.id == R.id.loginFragment) {
            findNavController().navigate(R.id.action_loginFragment_to_listaHabitosFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}