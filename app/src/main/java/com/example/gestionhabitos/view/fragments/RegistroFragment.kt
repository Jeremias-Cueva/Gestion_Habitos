    package com.example.gestionhabitos.view.fragments

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.navigation.fragment.findNavController
    import com.example.gestionhabitos.databinding.FragmentRegistroBinding
    import com.example.gestionhabitos.viewmodel.UsuarioViewModel

    class RegistroFragment : Fragment() {
        private var _binding: FragmentRegistroBinding? = null
        private val binding get() = _binding!!

        // Vinculamos el ViewModel de Usuario
        private val viewModel: UsuarioViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentRegistroBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.btnRegistrar.setOnClickListener {
                val nombre = binding.etNombreRegistro.text.toString().trim()
                val email = binding.etEmailRegistro.text.toString().trim()
                val pass = binding.etPasswordRegistro.text.toString().trim()

                if (nombre.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                    viewModel.registrar(nombre, email, pass)
                    Toast.makeText(requireContext(), "Enviando a SupaBase...", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }

            // Observamos el resultado del registro desde el ViewModel
            viewModel.registroExitoso.observe(viewLifecycleOwner) { exito ->
                if (exito == true) {
                    Toast.makeText(requireContext(), "¡Registro exitoso! Ya puedes entrar", Toast.LENGTH_LONG).show()
                    // Regresamos al Login automáticamente
                    findNavController().popBackStack()
                } else if (exito == false) {
                    Toast.makeText(requireContext(), "Error al registrar. Intenta de nuevo.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }