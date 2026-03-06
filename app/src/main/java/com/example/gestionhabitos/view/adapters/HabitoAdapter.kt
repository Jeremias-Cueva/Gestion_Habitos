package com.example.gestionhabitos.view.adapters

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionhabitos.R
import com.example.gestionhabitos.databinding.ItemHabitoBinding
import com.example.gestionhabitos.model.entitis.Habito

class HabitoAdapter(
    private val onHabitChecked: (Habito, Boolean) -> Unit,
    private val onHabitClick: (Habito) -> Unit
) : ListAdapter<Habito, HabitoAdapter.HabitoViewHolder>(HabitoDiffCallback()) {

    class HabitoViewHolder(val binding: ItemHabitoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitoViewHolder {
        val binding = ItemHabitoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HabitoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitoViewHolder, position: Int) {

        val habito = getItem(position)
        val context = holder.itemView.context

        holder.binding.tvHabitName.text = habito.nombre

        // 🔥 CATEGORÍAS CORRECTAS
        val categoriaNombre = when (habito.categoriaId) {
            1 -> "General"
            2 -> "Salud"
            3 -> "Estudio"
            4 -> "Trabajo"
            5 -> "Deporte"
            else -> "Sin categoría"
        }

        holder.binding.tvHabitCategory.text = "Categoría: $categoriaNombre"

        // Mostrar hora solo si existe
        if (habito.hora.isNotEmpty()) {
            holder.binding.tvHabitTime.text = habito.hora
            holder.binding.tvHabitTime.visibility = View.VISIBLE
        } else {
            holder.binding.tvHabitTime.visibility = View.GONE
        }

        // Color según estado
        val colorActual =
            if (habito.completado)
                ContextCompat.getColor(context, R.color.habit_completed_bg)
            else
                ContextCompat.getColor(context, R.color.surface_white)

        holder.binding.root.setCardBackgroundColor(colorActual)

        holder.binding.root.setOnClickListener {
            onHabitClick(habito)
        }

        holder.binding.cbHabitDone.setOnCheckedChangeListener(null)
        holder.binding.cbHabitDone.isChecked = habito.completado

        holder.binding.cbHabitDone.setOnCheckedChangeListener { _, isChecked ->

            val colorDesde =
                if (isChecked)
                    ContextCompat.getColor(context, R.color.surface_white)
                else
                    ContextCompat.getColor(context, R.color.habit_completed_bg)

            val colorHasta =
                if (isChecked)
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

    class HabitoDiffCallback : DiffUtil.ItemCallback<Habito>() {

        override fun areItemsTheSame(oldItem: Habito, newItem: Habito): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habito, newItem: Habito): Boolean {
            return oldItem == newItem
        }
    }
}