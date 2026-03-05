package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gestionhabitos.databinding.FragmentRecuperarPasswordBinding
import com.example.gestionhabitos.model.api.*
import com.example.gestionhabitos.model.entitis.Usuario
import com.example.gestionhabitos.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class RecuperarPasswordFragment : Fragment() {

    private var _binding: FragmentRecuperarPasswordBinding? = null
    private val binding get() = _binding!!
    
    private var codigoGenerado: String = ""
    private var usuarioEncontrado: Usuario? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecuperarPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEnviarCodigo.setOnClickListener {
            val email = binding.etEmailRecuperar.text.toString().trim()
            if (email.isNotEmpty()) {
                verificarYEnviarCorreo(email)
            } else {
                Toast.makeText(requireContext(), "Ingresa tu correo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnVerificarCodigo.setOnClickListener {
            val codigoIngresado = binding.etCodigo.text.toString().trim()
            if (codigoIngresado == codigoGenerado) {
                mostrarCamposNuevaPassword()
            } else {
                Toast.makeText(requireContext(), "Código incorrecto", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGuardarNuevaPassword.setOnClickListener {
            val nuevaPassword = binding.etNuevaPassword.text.toString().trim()
            if (nuevaPassword.isNotEmpty()) {
                actualizarPasswordEnServidor(nuevaPassword)
            } else {
                Toast.makeText(requireContext(), "Ingresa la nueva contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verificarYEnviarCorreo(email: String) {
        lifecycleScope.launch {
            try {
                val responseUser = withContext(Dispatchers.IO) {
                    RetrofitClient.habitFlow.buscarUsuarioPorEmail(email)
                }

                if (responseUser.isSuccessful && responseUser.body()?.isNotEmpty() == true) {
                    usuarioEncontrado = responseUser.body()?.first()
                    codigoGenerado = Random.nextInt(1000, 9999).toString()

                    val mailRequest = SendGridMailRequest(
                        personalizations = listOf(Personalization(listOf(EmailUser(email)))),
                        from = EmailUser(SendGridConfig.FROM_EMAIL),
                        subject = "Código de Recuperación - HabitFlow",
                        content = listOf(MailContent(value = "Tu código de recuperación es: $codigoGenerado"))
                    )

                    val responseMail = withContext(Dispatchers.IO) {
                        RetrofitClient.sendGrid.enviarCorreo(SendGridConfig.API_KEY, mailRequest)
                    }

                    if (responseMail.isSuccessful) {
                        Toast.makeText(requireContext(), "Código enviado a su correo", Toast.LENGTH_SHORT).show()
                        mostrarCamposCodigo()
                    } else {
                        Toast.makeText(requireContext(), "Error al enviar el correo", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "El correo no está registrado", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarPasswordEnServidor(nuevaPassword: String) {
        val user = usuarioEncontrado ?: return
        lifecycleScope.launch {
            try {
                val usuarioActualizado = user.copy(password = nuevaPassword)
                // MockAPI usa IDs como Strings en la URL, pero tu modelo lo tiene como Int. 
                // Habitualmente MockAPI maneja esto bien.
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.habitFlow.actualizarUsuario(user.id.toString(), usuarioActualizado)
                }

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Contraseña actualizada con éxito", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack() // Volver al Login
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarCamposCodigo() {
        binding.tilCodigo.visibility = View.VISIBLE
        binding.btnVerificarCodigo.visibility = View.VISIBLE
        binding.btnEnviarCodigo.visibility = View.GONE
        binding.tilEmailRecuperar.isEnabled = false
    }

    private fun mostrarCamposNuevaPassword() {
        binding.tilCodigo.visibility = View.GONE
        binding.btnVerificarCodigo.visibility = View.GONE
        binding.tilNuevaPassword.visibility = View.VISIBLE
        binding.btnGuardarNuevaPassword.visibility = View.VISIBLE
        binding.tvDescRecuperar.text = "Ingresa tu nueva contraseña a continuación."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}