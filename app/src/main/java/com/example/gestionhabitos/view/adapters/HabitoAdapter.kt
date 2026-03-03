package com.example.gestionhabitos.view.adapters

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.ItemHabitoBinding
import com.example.gestionhabitos.model.entitis.Habito

class HabitoAdapter(
    var listaHabitos: List<Habito>,
    private val onHabitChecked: (Habito, Boolean) -> Unit,
    private val onHabitClick: (Habito) -> Unit // Nueva función para manejar el click y editar
) : RecyclerView.Adapter<HabitoAdapter.HabitoViewHolder>() {

    class HabitoViewHolder(val binding: ItemHabitoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitoViewHolder {
        val binding = ItemHabitoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitoViewHolder, position: Int) {
        val habito = listaHabitos[position]
        val context = holder.itemView.context

        holder.binding.tvHabitName.text = habito.nombre
        holder.binding.tvHabitCategory.text = habito.categoria
        
        // Mostramos la hora si existe
        if (habito.hora.isNotEmpty()) {
            holder.binding.tvHabitTime.text = habito.hora
            holder.binding.tvHabitTime.visibility = android.view.View.VISIBLE
        } else {
            holder.binding.tvHabitTime.visibility = android.view.View.GONE
        }

        val colorInicial = if (habito.completado)
            ContextCompat.getColor(context, R.color.habit_completed_bg)
        else
            ContextCompat.getColor(context, R.color.surface_white)

        holder.binding.root.setCardBackgroundColor(colorInicial)

        // Manejar el click en el card para editar
        holder.binding.root.setOnClickListener {
            onHabitClick(habito)
        }

        holder.binding.cbHabitDone.setOnCheckedChangeListener(null)
        holder.binding.cbHabitDone.isChecked = habito.completado

        holder.binding.cbHabitDone.setOnCheckedChangeListener { _, isChecked ->
            val colorDesde = if (isChecked)
                ContextCompat.getColor(context, R.color.surface_white)
            else
                ContextCompat.getColor(context, R.color.habit_completed_bg)

            val colorHasta = if (isChecked)
                ContextCompat.getColor(context, R.color.habit_completed_bg)
            else
                ContextCompat.getColor(context, R.color.surface_white)

            ValueAnimator.ofObject(ArgbEvaluator(), colorDesde, colorHasta).apply {
                duration = 300
                addUpdateListener { animator ->
                    holder.binding.root.setCardBackgroundColor(animator.animatedValue as Int)
                }
                start()
            }

            onHabitChecked(habito, isChecked)
        }
    }

    override fun getItemCount(): Int = listaHabitos.size

    fun updateList(nuevaLista: List<Habito>) {
        this.listaHabitos = nuevaLista
        notifyDataSetChanged()
    }
}