package com.example.gestionhabitos.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionhabitos.databinding.ItemObjetivoBinding
import com.example.gestionhabitos.model.entitis.Objetivo

class ObjetivoAdapter(
    private val onEditClick: (Objetivo) -> Unit,   // 🛡️ SOLUCIÓN: Parámetro Edit
    private val onDeleteClick: (Objetivo) -> Unit  // 🛡️ SOLUCIÓN: Parámetro Delete
) : ListAdapter<Objetivo, ObjetivoAdapter.ObjetivoViewHolder>(ObjetivoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjetivoViewHolder {
        val binding = ItemObjetivoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjetivoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ObjetivoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ObjetivoViewHolder(private val binding: ItemObjetivoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(objetivo: Objetivo) {
            binding.tvTituloObjetivo.text = objetivo.titulo
            binding.tvProgreso.text = "${objetivo.valorActual} / ${objetivo.metaValor}"

            // Lógica de los botones
            binding.btnEliminar.setOnClickListener { onDeleteClick(objetivo) }
            binding.root.setOnClickListener { onEditClick(objetivo) }
        }
    }

    class ObjetivoDiffCallback : DiffUtil.ItemCallback<Objetivo>() {
        override fun areItemsTheSame(oldItem: Objetivo, newItem: Objetivo) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Objetivo, newItem: Objetivo) = oldItem == newItem
    }
}