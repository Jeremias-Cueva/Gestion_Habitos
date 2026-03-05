package com.example.gestionhabitos.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.gestionhabitos.databinding.FragmentRecuperarPasswordBinding
import com.example.gestionhabitos.model.api.*
import com.example.gestionhabitos.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class RecuperarPasswordFragment : Fragment() {

    private var _binding: FragmentRecuperarPasswordBinding? = null
    private val binding get() = _binding!!
    
    private var codigoGenerado: String = ""

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
                Toast.makeText(requireContext(), "Código correcto. Ahora puedes cambiar tu clave.", Toast.LENGTH_LONG).show()
                // Aquí podrías navegar a una pantalla de "Cambiar Contraseña"
            } else {
                Toast.makeText(requireContext(), "Código incorrecto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verificarYEnviarCorreo(email: String) {
        lifecycleScope.launch {
            try {
                // 1. Verificar si el usuario existe en MockAPI
                val responseUser = withContext(Dispatchers.IO) {
                    RetrofitClient.habitFlow.buscarUsuarioPorEmail(email)
                }

                if (responseUser.isSuccessful && responseUser.body()?.isNotEmpty() == true) {
                    // 2. Generar código de 4 dígitos
                    codigoGenerado = Random.nextInt(1000, 9999).toString()

                    // 3. Preparar el correo para SendGrid
                    val mailRequest = SendGridMailRequest(
                        personalizations = listOf(Personalization(listOf(EmailUser(email)))),
                        from = EmailUser(SendGridConfig.FROM_EMAIL),
                        subject = "Código de Recuperación - HabitFlow",
                        content = listOf(MailContent(value = "Tu código de recuperación es: $codigoGenerado"))
                    )

                    // 4. Enviar usando SendGrid
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

    private fun mostrarCamposCodigo() {
        binding.tilCodigo.visibility = View.VISIBLE
        binding.btnVerificarCodigo.visibility = View.VISIBLE
        binding.btnEnviarCodigo.visibility = View.GONE
        binding.tilEmailRecuperar.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}