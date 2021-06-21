package pan.alexander.filmrevealer.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS
import pan.alexander.filmrevealer.R
import pan.alexander.filmrevealer.databinding.FragmentFilmDetailsBinding
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import pan.alexander.filmrevealer.presentation.viewmodels.FilmDetailsViewModel
import java.util.*

private const val UNDEFINED_FILM_ID = -1
private const val LOAD_DETAILS_RETRY_COUNT = 3

class FilmDetailsFragment : Fragment() {

    private lateinit var viewModel: FilmDetailsViewModel
    private var _binding: FragmentFilmDetailsBinding? = null
    private val binding get() = _binding!!
    private var filmId = UNDEFINED_FILM_ID
    private var filmPosterUrl = ""
    private var loadDetailsRetryCounter = LOAD_DETAILS_RETRY_COUNT

    companion object {
        const val BUNDLE_EXTRA = "film"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(FilmDetailsViewModel::class.java)
        _binding = FragmentFilmDetailsBinding.inflate(inflater, container, false)

        useFilmDataFromArguments()

        return binding.root
    }

    private fun useFilmDataFromArguments() {
        arguments?.let { bundle ->
            bundle.getParcelable<Film>(BUNDLE_EXTRA)?.let { film ->
                filmId = film.movieId
                binding.textViewTitle.text = film.title
                binding.textViewReleaseDateValue.text = film.releaseDate
                filmPosterUrl = film.posterUrl
                context?.let { loadPoster(it) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayDefaultValues()

        if (filmId == UNDEFINED_FILM_ID) {
            return
        }

        viewModel.getFilmDetailsLiveData(filmId).observe(viewLifecycleOwner, {

            if (it.isNotEmpty()) {
                displayData(it[0])
            }

            if (it.isEmpty()
                || System.currentTimeMillis() - it[0].timeStamp > FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS
            ) {
                requestUpdates()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun displayDefaultValues() = with(binding) {
        val loadingStr = getString(R.string.loading)
        textViewOriginalTitle.text = loadingStr
        textViewGenres.text = loadingStr
        textViewRuntime.text = "0"
        textViewRating.text = "0.0 (0)"
        textViewBudgetValue.text = "0 $"
        textViewRevenueValue.text = "0 $"
    }

    private fun requestUpdates() {
        if (loadDetailsRetryCounter > 0) {
            viewModel.loadFilmDetails(filmId)
            loadDetailsRetryCounter--
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayData(filmDetails: FilmDetails) {
        binding.textViewTitle.text = filmDetails.title
        binding.textViewOriginalTitle.text = "${filmDetails.originalTitle} " +
                "(${filmDetails.releaseDate.split("-")[0]})"
        binding.textViewGenres.text = filmDetails.genres.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        binding.textViewRuntime.text = filmDetails.runtime.toString()
        binding.textViewRating.text = "${filmDetails.voteAverage} (${filmDetails.voteCount})"
        binding.textViewBudgetValue.text = "${filmDetails.budget} $"
        binding.textViewRevenueValue.text = "${filmDetails.revenue} $"
        binding.textViewReleaseDateValue.text = filmDetails.releaseDate
        binding.textViewDescription.text = filmDetails.overview

        if (filmDetails.posterUrl != filmPosterUrl
            && filmDetails.posterUrl.isNotBlank()
        ) {
            filmPosterUrl = filmDetails.posterUrl
            context?.let { context -> loadPoster(context) }
        }
    }

    private fun loadPoster(context: Context) {
        binding.imageViewPoster.scaleType = ImageView.ScaleType.CENTER
        Glide.with(context)
            .load(App.POSTER_BASE_URL + filmPosterUrl)
            .override(binding.imageViewPoster.width, binding.imageViewPoster.height)
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
                    _binding?.imageViewPoster?.scaleType = ImageView.ScaleType.FIT_CENTER
                    _binding?.root?.requestLayout()
                    return false
                }

            })
            .into(binding.imageViewPoster)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        loadDetailsRetryCounter = LOAD_DETAILS_RETRY_COUNT
    }
}
