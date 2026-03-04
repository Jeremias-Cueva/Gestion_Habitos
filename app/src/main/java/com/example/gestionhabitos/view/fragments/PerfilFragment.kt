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

            // Observar datos del usuario logueado (ID 1)
            usuarioViewModel.datosUsuario.observe(viewLifecycleOwner) { usuario ->
                usuario?.let {
                    binding.tvUserNameProfile.text = it.nombre
                    binding.tvUserEmail.text = it.email
                }
            }

            binding.btnEditProfile.setOnClickListener { mostrarDialogoEdicion() }

            // LÓGICA DE CIERRE DE SESIÓN
// LÓGICA DE CIERRE DE SESIÓN
            binding.btnLogoutProfile?.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Cerrar Sesión")
                    .setMessage("¿Estás seguro de que deseas salir de HabitFlow?")
// Dentro del setPositiveButton de tu PerfilFragment.kt

                    .setPositiveButton("Salir") { _, _ ->
                        // 1. Borramos los datos locales en Room
                        usuarioViewModel.logout()

                        // 2. Navegamos al Login eliminando todo el historial anterior
                        findNavController().navigate(
                            R.id.action_perfilFragment_to_loginFragment,
                            null,
                            androidx.navigation.NavOptions.Builder()
                                .setPopUpTo(R.id.nav_graph, true) // Esto limpia el stack de navegación
                                .build()
                        )

                        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
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
                    val nombre = etNombre.text.toString().trim()
                    val email = etCorreo.text.toString().trim()
                    if (nombre.isNotEmpty() && email.isNotEmpty()) {
                        // Actualizamos Room manteniendo el ID 1
                        val usuarioActual = usuarioViewModel.datosUsuario.value
                        usuarioViewModel.actualizarUsuario(Usuario(
                            id = 1,
                            nombre = nombre,
                            email = email,
                            password = usuarioActual?.password ?: ""
                        ))
                        Toast.makeText(requireContext(), "Datos actualizados", Toast.LENGTH_SHORT).show()
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