package pan.alexander.filmrevealer.presentation.fragments

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.distinctUntilChanged
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pan.alexander.filmrevealer.*
import pan.alexander.filmrevealer.databinding.FragmentFilmDetailsBinding
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import pan.alexander.filmrevealer.presentation.viewmodels.FilmDetailsViewModel
import pan.alexander.filmrevealer.services.RateFilmIntentService
import pan.alexander.filmrevealer.utils.InternetConnectionLiveData
import pan.alexander.filmrevealer.utils.ConnectionUtils
import pan.alexander.filmrevealer.utils.showSnackBar
import java.util.*

private const val LOAD_DETAILS_RETRY_COUNT = 3

@ExperimentalCoroutinesApi
class FilmDetailsFragment : BaseFragment(), View.OnClickListener {

    private val viewModel by lazy { ViewModelProvider(this).get(FilmDetailsViewModel::class.java) }
    private var _binding: FragmentFilmDetailsBinding? = null
    private val binding get() = _binding!!
    private var filmFromArguments: Film? = null
    private var loadDetailsRetryCounter = LOAD_DETAILS_RETRY_COUNT
    private var ratingStarsList: List<ImageView>? = null
    private var ratedFilm: Film? = null

    private val starRatedColor by lazy {
        context?.let {
            ContextCompat.getColor(
                it, R.color.star_rated
            )
        }
    }
    private val starNotRatedColor by lazy {
        context?.let {
            ContextCompat.getColor(
                it, R.color.star_not_rated
            )
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == RATE_FILM_ERROR_ACTION) {
                    it.getStringExtra(EXTRA_PARAM_ERROR)
                        ?.takeIf { message -> message.isNotBlank() }
                        ?.let { message ->
                            binding.root.showSnackBar(message)
                        }
                }
            }
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "film"
        const val RATE_FILM_ERROR_ACTION =
            "pan.alexander.filmrevealer.presentation.fragments.action.RATE_FILM_ERROR"
        const val EXTRA_PARAM_ERROR =
            "pan.alexander.filmrevealer.presentation.fragments.extra.PARAM_ERROR"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmDetailsBinding.inflate(inflater, container, false)

        useFilmDataFromArguments()

        setRatingStarsOnClickListener()

        return binding.root
    }

    private fun useFilmDataFromArguments() {
        arguments?.let { bundle ->
            bundle.getParcelable<Film>(BUNDLE_EXTRA)?.let { film ->
                binding.textViewTitle.text = film.title
                binding.textViewReleaseDateValue.text = film.releaseDate
                this.filmFromArguments = film
                context?.let { loadPoster(it) }
            }
        }
    }

    private fun setRatingStarsOnClickListener() = with(binding) {
        ratingStarsList = listOf(
            imageViewRatingStar1,
            imageViewRatingStar2,
            imageViewRatingStar3,
            imageViewRatingStar4,
            imageViewRatingStar5,
            imageViewRatingStar6,
            imageViewRatingStar7,
            imageViewRatingStar8,
            imageViewRatingStar9,
            imageViewRatingStar10,
        )
        ratingStarsList?.forEach { it.setOnClickListener(this@FilmDetailsFragment) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayDefaultValues()

        lifecycle.addObserver(viewModel)

        registerRateFilmErrorBroadcastReceiver()

        observeInternetConnectionAvailable()

        observeFilmDetails()

        observeRatedFilmLiveData()

        observeLoadingFailure(viewModel, binding.root)
    }

    private fun registerRateFilmErrorBroadcastReceiver() {
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(broadcastReceiver, IntentFilter(RATE_FILM_ERROR_ACTION))
        }
    }

    private fun observeInternetConnectionAvailable() {
        context?.let {
            InternetConnectionLiveData.distinctUntilChanged()
                .observe(viewLifecycleOwner) { connected ->
                if (connected) {
                    filmFromArguments?.let { film ->
                        viewModel.getFilmDetailsLiveData(film.movieId).value?.let { filmDetails ->
                            updateFilmDetails(film, filmDetails)
                        }
                    }
                }
            }
        }
    }

    private fun observeFilmDetails() {
        filmFromArguments?.let { film ->
            viewModel.getFilmDetailsLiveData(film.movieId)
                .observe(viewLifecycleOwner, {
                    updateFilmDetails(film, it)
                })
        }
    }

    private fun updateFilmDetails(filmFromArguments: Film, filmDetails: List<FilmDetails>) {
        if (filmDetails.isNotEmpty()) {
            displayData(filmDetails.first())
        }

        if (filmDetails.isEmpty() || isUpdateRequired(filmDetails.first().timeStamp)) {
            requestUpdates(filmFromArguments)
        }
    }

    private fun observeRatedFilmLiveData() {

        filmFromArguments?.let { film ->
            viewModel.getRatedFilmLiveData(film.movieId)
                .observe(viewLifecycleOwner, { ratedFilm ->
                    ratedFilm.takeIf { it.isNotEmpty() }.let {
                        val userRating = it?.first()?.userRating ?: 0

                        if (userRating > 0) {
                            this.ratedFilm = it?.first()
                        }

                        ratingStarsList?.forEachIndexed { index, imageView ->
                            changeStarColor(imageView, index < userRating)
                        }
                    }
                })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayDefaultValues() = with(binding) {
        val loadingStr = getString(R.string.loading)
        textViewOriginalTitle.text = loadingStr
        textViewGenres.text = loadingStr
        textViewRuntime.text = "0"
        textViewVotes.text = "0.0 (0)"
        textViewBudgetValue.text = "0 $"
        textViewRevenueValue.text = "0 $"
    }

    private fun requestUpdates(film: Film) {

        if (loadDetailsRetryCounter > 0) {
            viewModel.loadFilmDetails(film.movieId)
            loadDetailsRetryCounter--
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayData(filmDetails: FilmDetails) = with(binding) {
        textViewTitle.text = filmDetails.title
        textViewOriginalTitle.text = "${filmDetails.originalTitle} " +
                "(${filmDetails.releaseDate.split("-").first()})"
        textViewGenres.text = filmDetails.genres.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        textViewRuntime.text = filmDetails.runtime.toString()
        textViewVotes.text = "${filmDetails.voteAverage} (${filmDetails.voteCount})"
        textViewBudgetValue.text = "${filmDetails.budget} $"
        textViewRevenueValue.text = "${filmDetails.revenue} $"
        textViewReleaseDateValue.text = filmDetails.releaseDate
        textViewDescription.text = filmDetails.overview

        if (filmDetails.posterUrl != filmFromArguments?.posterUrl
            && filmDetails.posterUrl.isNotBlank()
        ) {
            filmFromArguments?.posterUrl = filmDetails.posterUrl
            context?.let { context -> loadPoster(context) }
        }
    }

    private fun loadPoster(context: Context) {
        binding.imageViewPoster.scaleType = ImageView.ScaleType.CENTER
        Glide.with(context)
            .load(BuildConfig.POSTER_BASE_URL + filmFromArguments?.posterUrl)
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

        unregisterRateFilmErrorBroadcastReceiver()

        ratingStarsList = null
        _binding = null
        loadDetailsRetryCounter = LOAD_DETAILS_RETRY_COUNT
    }

    private fun unregisterRateFilmErrorBroadcastReceiver() {
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .unregisterReceiver(broadcastReceiver)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageViewRatingStar1 -> changeFilmRating(1)
            R.id.imageViewRatingStar2 -> changeFilmRating(2)
            R.id.imageViewRatingStar3 -> changeFilmRating(3)
            R.id.imageViewRatingStar4 -> changeFilmRating(4)
            R.id.imageViewRatingStar5 -> changeFilmRating(5)
            R.id.imageViewRatingStar6 -> changeFilmRating(6)
            R.id.imageViewRatingStar7 -> changeFilmRating(7)
            R.id.imageViewRatingStar8 -> changeFilmRating(8)
            R.id.imageViewRatingStar9 -> changeFilmRating(9)
            R.id.imageViewRatingStar10 -> changeFilmRating(10)
        }
    }

    private fun changeFilmRating(rating: Int) {

        val context = context ?: return

        if (!ConnectionUtils.isInternetAvailable(context)) {
            binding.root.showSnackBar(context.getString(R.string.internet_not_available))
            return
        }

        val filmToRate = ratedFilm ?: filmFromArguments

        filmToRate?.apply {
            if (this.userRating == 0) {
                id = 0
                page = 1
                totalPages = 1
                section = Film.Section.USER_RATED.value
            }
            userRating = rating
        }?.let {
            RateFilmIntentService.rateFilm(context, it, rating.toFloat())
        }

        ratingStarsList?.forEachIndexed { index, imageView ->
            changeStarColor(imageView, index < rating)
        }
    }

    private fun changeStarColor(imageView: ImageView, rated: Boolean) {
        if (rated) {
            starRatedColor?.let {
                imageView.drawable.mutate().colorFilter = BlendModeColorFilterCompat
                    .createBlendModeColorFilterCompat(it, BlendModeCompat.SRC_ATOP)
            }
        } else {
            starNotRatedColor?.let {
                imageView.drawable.mutate().colorFilter = BlendModeColorFilterCompat
                    .createBlendModeColorFilterCompat(it, BlendModeCompat.SRC_ATOP)
            }
        }
    }
}
