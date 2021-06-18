package pan.alexander.filmrevealer.presentation.recycler

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pan.alexander.filmrevealer.R
import pan.alexander.filmrevealer.databinding.RecyclerItemFilmBinding
import pan.alexander.filmrevealer.domain.entities.Film

class FilmsViewHolder(
    private val binding: RecyclerItemFilmBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(context: Context, film: Film) {
        Glide.with(context)
            .load(film.posterUrl)
            .placeholder(R.drawable.ic_baseline_cached_24)
            .error(R.drawable.ic_baseline_warning_24)
            .into(binding.imageViewFilmPoster)
        binding.textViewTitle.text = film.title
        binding.textViewYear.text = film.releaseDate
        binding.textViewRating.text = film.rating.toString()
    }
}
