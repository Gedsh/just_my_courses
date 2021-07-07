package pan.alexander.filmrevealer.presentation.fragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pan.alexander.filmrevealer.*
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.databinding.FragmentHomeBinding
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.Failure
import pan.alexander.filmrevealer.presentation.recycler.FilmsAdapter
import pan.alexander.filmrevealer.presentation.recycler.OnRecyclerScrolledListener
import pan.alexander.filmrevealer.presentation.viewmodels.HomeViewModel
import pan.alexander.filmrevealer.utils.InternetConnectionLiveData
import pan.alexander.filmrevealer.utils.Utils

class HomeFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var nowPlayingFilmsAdapter: FilmsAdapter? = null
    private var upcomingFilmsAdapter: FilmsAdapter? = null

    private var nowPlayingRecycleViewState: Parcelable? = null
    private var upcomingRecycleViewState: Parcelable? = null

    private val onDataRequiredListener by lazy {
        fun(section: Film.Section, page: Int) {
            when (section) {
                Film.Section.NOW_PLAYING -> viewModel.updateNowPlayingFilms(page)
                Film.Section.UPCOMING -> viewModel.updateUpcomingFilms(page)
                else -> Log.e(LOG_TAG, "Section $section is not supported in HomeFragment")
            }
        }
    }

    private val onFilmClickListener by lazy {
        fun(view: View?, film: Film) {
            val bundle = bundleOf(FilmDetailsFragment.BUNDLE_EXTRA to film)
            view?.findNavController()?.navigate(R.id.navigation_to_film_details, bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initNowPlayingFilmsRecycler()

        initUpcomingFilmsRecycler()

        nowPlayingRecycleViewState?.let {
            binding.recyclerViewNowPlaying.layoutManager?.onRestoreInstanceState(it)
        }

        upcomingRecycleViewState?.let {
            binding.recyclerViewUpcoming.layoutManager?.onRestoreInstanceState(it)
        }

        return binding.root
    }

    private fun initNowPlayingFilmsRecycler() = with(binding) {
        recyclerViewNowPlaying.setHasFixedSize(true)
        nowPlayingFilmsAdapter = context?.let { FilmsAdapter(it) }!!
        nowPlayingFilmsAdapter?.setHasStableIds(true)
        nowPlayingFilmsAdapter?.onDataRequiredListener = onDataRequiredListener
        nowPlayingFilmsAdapter?.onFilmClickListener = onFilmClickListener
        recyclerViewNowPlaying.adapter = nowPlayingFilmsAdapter

        val layoutManager = recyclerViewNowPlaying.layoutManager as? LinearLayoutManager
        val recyclerScrolledListener = nowPlayingFilmsAdapter as? OnRecyclerScrolledListener
        recyclerViewNowPlaying.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx > MINIMUM_SCROLL_DIFFERENCE) {
                    layoutManager?.findLastVisibleItemPosition()?.let {
                        recyclerScrolledListener?.onRecyclerScrolled(it)
                    }
                }
            }
        })
    }

    private fun initUpcomingFilmsRecycler() = with(binding) {
        recyclerViewUpcoming.setHasFixedSize(true)
        upcomingFilmsAdapter = context?.let { FilmsAdapter(it) }
        upcomingFilmsAdapter?.setHasStableIds(true)
        upcomingFilmsAdapter?.onDataRequiredListener = onDataRequiredListener
        upcomingFilmsAdapter?.onFilmClickListener = onFilmClickListener
        recyclerViewUpcoming.adapter = upcomingFilmsAdapter

        val layoutManager = recyclerViewUpcoming.layoutManager as? LinearLayoutManager
        val recyclerScrolledListener = upcomingFilmsAdapter as? OnRecyclerScrolledListener
        recyclerViewUpcoming.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx > MINIMUM_SCROLL_DIFFERENCE) {
                    layoutManager?.findLastVisibleItemPosition()?.let {
                        recyclerScrolledListener?.onRecyclerScrolled(it)
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        binding.recyclerViewNowPlaying.requestFocus()

        observeNowPlayingFilms()

        observeUpcomingFilms()

        observeLoadingFailure()

        observeInternetConnectionAvailable()
    }

    private fun observeInternetConnectionAvailable() {
        context?.let {
            InternetConnectionLiveData.observe(viewLifecycleOwner) { connected ->
                if (connected) {
                    viewModel.listOfNowPlayingFilmsLiveData.value?.let { updateNowPlayingFilms(it) }
                    viewModel.listOfUpcomingFilmsLiveData.value?.let { updateUpcomingFilms(it) }
                }
            }
        }
    }

    private fun observeNowPlayingFilms() {
        viewModel.listOfNowPlayingFilmsLiveData.observe(viewLifecycleOwner, { films ->
            updateNowPlayingFilms(films)
        })
    }

    private fun updateNowPlayingFilms(films: List<Film>) {
        if (films.isNotEmpty()) {
            val recycleViewState =
                binding.recyclerViewNowPlaying.layoutManager?.onSaveInstanceState()

            nowPlayingFilmsAdapter?.updateItems(films)

            val layoutManager = binding.recyclerViewNowPlaying.layoutManager as LinearLayoutManager
            val lastVisibleFilm =
                films.getOrElse(layoutManager.findLastVisibleItemPosition()) { films[0] }

            if (isUpdateRequired(lastVisibleFilm.timeStamp)) {
                viewModel.updateNowPlayingFilms(lastVisibleFilm.page)
            }

            binding.recyclerViewNowPlaying.layoutManager?.onRestoreInstanceState(
                recycleViewState
            )
        } else {
            viewModel.updateNowPlayingFilms(FIRST_PAGE_NUMBER)
        }
    }

    private fun observeUpcomingFilms() {
        viewModel.listOfUpcomingFilmsLiveData.observe(viewLifecycleOwner, { films ->
            updateUpcomingFilms(films)
        })
    }

    private fun updateUpcomingFilms(films: List<Film>) {
        if (films.isNotEmpty()) {
            val recycleViewState =
                binding.recyclerViewUpcoming.layoutManager?.onSaveInstanceState()

            upcomingFilmsAdapter?.updateItems(films)

            val layoutManager = binding.recyclerViewUpcoming.layoutManager as LinearLayoutManager
            val lastVisibleFilm =
                films.getOrElse(layoutManager.findLastVisibleItemPosition()) { films[0] }

            if (isUpdateRequired(lastVisibleFilm.timeStamp)) {
                viewModel.updateUpcomingFilms(lastVisibleFilm.page)
            }

            binding.recyclerViewUpcoming.layoutManager?.onRestoreInstanceState(
                recycleViewState
            )
        } else {
            viewModel.updateUpcomingFilms(FIRST_PAGE_NUMBER)
        }
    }

    private fun isUpdateRequired(filmTimestamp: Long): Boolean {
        return (System.currentTimeMillis() - filmTimestamp).toInt() > FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS
    }

    private fun observeLoadingFailure() {
        viewModel.failureLiveData.observe(viewLifecycleOwner, { failure ->

            context?.let { context ->
                if (Utils.isInternetAvailable(context)) {
                    when (failure) {
                        is Failure.WithMessageAndAction ->
                            failure.message.takeIf { it.isNotBlank() }?.let { message ->
                                binding.root.showSnackBar(message, R.string.retry, {
                                    failure.block()
                                })
                            }

                        is Failure.WithMessage ->
                            failure.message.takeIf { it.isNotBlank() }?.let { message ->
                                binding.root.showSnackBar(message)
                            }
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        nowPlayingRecycleViewState =
            binding.recyclerViewNowPlaying.layoutManager?.onSaveInstanceState()
        upcomingRecycleViewState = binding.recyclerViewUpcoming.layoutManager?.onSaveInstanceState()

        nowPlayingFilmsAdapter?.onDataRequiredListener = null
        nowPlayingFilmsAdapter?.onFilmClickListener = null
        upcomingFilmsAdapter?.onDataRequiredListener = null
        upcomingFilmsAdapter?.onFilmClickListener = null
        _binding = null
    }

}
