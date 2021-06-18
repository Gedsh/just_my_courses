package pan.alexander.filmrevealer.presentation.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pan.alexander.filmrevealer.databinding.RecyclerItemFilmBinding
import pan.alexander.filmrevealer.domain.entities.Film

class FilmsAdapter(
    private val context: Context
) : RecyclerView.Adapter<FilmsViewHolder>() {
    private var items: MutableList<Film> = mutableListOf()

    fun updateItems(films: List<Film>) {
        items.clear()
        items.addAll(films)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        val binding = RecyclerItemFilmBinding.inflate(LayoutInflater.from(context), parent, false)
        return FilmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        holder.bind(context, items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
