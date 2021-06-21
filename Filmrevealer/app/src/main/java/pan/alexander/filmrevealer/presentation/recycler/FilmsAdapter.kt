package pan.alexander.filmrevealer.presentation.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pan.alexander.filmrevealer.FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS
import pan.alexander.filmrevealer.databinding.RecyclerItemFilmBinding
import pan.alexander.filmrevealer.domain.entities.Film

class FilmsAdapter(
    private val context: Context
) : RecyclerView.Adapter<FilmsViewHolder>(), OnRecyclerScrolledListener {

    interface OnDataRequiredListener {
        fun onDataRequired(section: Film.Section, page: Int)
    }

    var onDataRequiredListener: OnDataRequiredListener? = null
    var onFilmClickListener: OnFilmClickListener? = null
    private val items: MutableList<Film> = mutableListOf()

    fun updateItems(films: List<Film>) {
        if (items.isNotEmpty()) {
            for (i in films.indices) {
                val existingItem = items.getOrNull(i)
                when {
                    existingItem == null -> {
                        items.add(i, films[i])
                        notifyItemInserted(i)
                    }
                    films[i] != existingItem -> {
                        items[i] = films[i]
                        notifyItemChanged(i)
                    }
                    else -> {
                        items[i] = films[i]
                    }
                }
            }

            val itemsSize = items.size
            val filmsSize = films.size
            if (itemsSize > filmsSize) {
                val subListToDelete = items.subList(filmsSize, itemsSize)
                items.removeAll(subListToDelete)
                notifyItemRangeRemoved(filmsSize, itemsSize)
            }

        } else {
            items.addAll(films)
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        val binding = RecyclerItemFilmBinding.inflate(LayoutInflater.from(context), parent, false)
        val viewHolder = FilmsViewHolder(binding, onFilmClickListener, items)
        binding.cardViewFilm.setOnClickListener(viewHolder)
        return viewHolder
    }

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        holder.bind(context, items[position])
    }

    private fun getSectionForSectionValue(value: Int): Film.Section {
        var filmSection = Film.Section.NOW_PLAYING
        Film.Section.values().forEach { section ->
            if (value == section.value) {
                filmSection = section
            }
        }
        return filmSection
    }

    override fun getItemCount() = items.size

    override fun onRecyclerScrolled(lastVisibleItemPosition: Int) {
        loadNextPageIfNecessary(lastVisibleItemPosition)
    }

    private fun loadNextPageIfNecessary(position: Int) {
        if (items.getOrNull(position + 1) != null) {
            val currentFilm = items[position]
            val nextFilm = items[position + 1]
            if (nextFilm.page != currentFilm.page
                && (System.currentTimeMillis() - nextFilm.timeStamp).toInt() > FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS
            ) {
                val nextFilmSection = getSectionForSectionValue(nextFilm.section)
                onDataRequiredListener?.onDataRequired(nextFilmSection, nextFilm.page)
            }
        } else {
            val lastFilm = items[position]
            if (lastFilm.page < lastFilm.totalPages) {
                val lastFilmSection = getSectionForSectionValue(lastFilm.section)
                onDataRequiredListener?.onDataRequired(lastFilmSection, lastFilm.page + 1)
            }
        }
    }

}
