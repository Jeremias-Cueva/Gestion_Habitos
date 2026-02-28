package com.example.gestionhabitos.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionhabitos.databinding.ItemHabitoBinding
import com.example.gestionhabitos.model.entitis.Habito

class HabitoAdapter(private var listaHabitos: List<Habito>) :
    RecyclerView.Adapter<HabitoAdapter.HabitoViewHolder>() {

    // El ViewHolder es el contenedor de la vista de cada fila
    class HabitoViewHolder(val binding: ItemHabitoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitoViewHolder {
        val binding = ItemHabitoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HabitoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitoViewHolder, position: Int) {
        val habito = listaHabitos[position]
        // Vinculamos los datos de la entidad con los TextViews del diseño item_habito.xml
        holder.binding.tvHabitName.text = habito.nombre
        holder.binding.tvHabitCategory.text = habito.categoria
        holder.binding.cbHabitDone.isChecked = habito.completado
    }

    override fun getItemCount(): Int = listaHabitos.size

    // Este método es vital para que la pantalla se actualice cuando observes cambios en el ViewModel
    fun updateList(nuevaLista: List<Habito>) {
        this.listaHabitos = nuevaLista
        notifyDataSetChanged() // Refresca la lista completa en pantalla
    }
}