package com.example.gestionhabitos.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionhabitos.databinding.ItemObjetivoBinding
import com.example.gestionhabitos.model.entitis.Objetivo
import java.util.*

class ObjetivoAdapter(
    private var listaObjetivos: List<Objetivo>,
    private val onObjetivoClick: (Objetivo) -> Unit
) : RecyclerView.Adapter<ObjetivoAdapter.ObjetivoViewHolder>() {

    class ObjetivoViewHolder(val binding: ItemObjetivoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjetivoViewHolder {
        val binding = ItemObjetivoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjetivoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ObjetivoViewHolder, position: Int) {
        val objetivo = listaObjetivos[position]
        
        holder.binding.apply {
            tvTituloObjetivo.text = objetivo.titulo
            tvDescripcionObjetivo.text = objetivo.descripcion
            
            // Calcular progreso
            val progreso = if (objetivo.metaValor > 0) {
                (objetivo.valorActual * 100 / objetivo.metaValor).toInt()
            } else 0
            
            progressObjetivo.progress = progreso
            tvPorcentajeObjetivo.text = "$progreso%"
            tvProgresoTexto.text = String.format(Locale.getDefault(), "%.1f de %.1f", objetivo.valorActual, objetivo.metaValor)
            
            root.setOnClickListener { onObjetivoClick(objetivo) }
        }
    }

    override fun getItemCount(): Int = listaObjetivos.size

    fun updateList(nuevaLista: List<Objetivo>) {
        listaObjetivos = nuevaLista
        notifyDataSetChanged()
    }
}