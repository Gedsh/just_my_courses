package pan.alexander.filmrevealer.presentation.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pan.alexander.filmrevealer.FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS
import pan.alexander.filmrevealer.databinding.RecyclerItemFilmBinding
import pan.alexander.filmrevealer.domain.entities.Film

class FilmsAdapter(
    private val context: Context
) : RecyclerView.Adapter<FilmsViewHolder>(), OnRecyclerScrolledListener {

    companion object {
        const val UNDEFINED_POSITION = -1
    }

    var onDataRequiredListener: ((section: Film.Section, page: Int) -> Unit)? = null
    var onFilmClickListener: ((view: View?, film: Film) -> Unit)? = null
    var lastVisibleFilmPosition: Int = UNDEFINED_POSITION
        private set

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

    override fun getItemId(position: Int): Long {
        return items[position].movieId.toLong()
    }

    override fun getItemCount() = items.size

    override fun onRecyclerScrolled(lastVisibleItemPosition: Int) =
        loadNextPageIfNecessary(lastVisibleItemPosition)

    private fun loadNextPageIfNecessary(position: Int) {
        if (items.getOrNull(position + 1) != null) {
            val currentFilm = items[position]
            lastVisibleFilmPosition = position
            val nextFilm = items[position + 1]
            if (nextFilm.page != currentFilm.page && isUpdateRequired(nextFilm.timeStamp)
            ) {
                val nextFilmSection = getSectionForSectionValue(nextFilm.section)
                onDataRequiredListener?.let { it(nextFilmSection, nextFilm.page) }
            }
        } else {
            val lastFilm = items[position]
            lastVisibleFilmPosition = position
            if (lastFilm.page < lastFilm.totalPages) {
                val lastFilmSection = getSectionForSectionValue(lastFilm.section)
                onDataRequiredListener?.let { it(lastFilmSection, lastFilm.page + 1) }
            }
        }
    }

    private fun isUpdateRequired(filmTimestamp: Long): Boolean {
        return (System.currentTimeMillis() - filmTimestamp).toInt() > FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS
    }

}
