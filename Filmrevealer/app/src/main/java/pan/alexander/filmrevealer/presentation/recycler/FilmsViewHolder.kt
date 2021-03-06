package pan.alexander.filmrevealer.presentation.recycler

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.R
import pan.alexander.filmrevealer.databinding.RecyclerItemFilmBinding
import pan.alexander.filmrevealer.domain.entities.Film


class FilmsViewHolder(
    private val binding: RecyclerItemFilmBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(context: Context, film: Film) = with(binding) {

        imageViewFilmPoster.scaleType = ImageView.ScaleType.CENTER

        Glide.with(context)
            .load(App.POSTER_BASE_URL + film.posterUrl)
            .override(binding.imageViewFilmPoster.width, binding.imageViewFilmPoster.height)
            .placeholder(R.drawable.ic_baseline_cached_24)
            .error(R.drawable.ic_baseline_warning_24)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageViewFilmPoster.scaleType = ImageView.ScaleType.FIT_XY
                    imageViewFilmPoster.requestLayout()
                    return false
                }

            })
            .into(imageViewFilmPoster)

        textViewTitle.text = film.title
        textViewYear.text = film.releaseDate
        textViewRating.text = String.format("%.1f", film.rating)
    }
}
