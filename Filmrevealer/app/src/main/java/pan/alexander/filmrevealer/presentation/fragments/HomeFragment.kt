package pan.alexander.filmrevealer.presentation.fragments

import android.os.Bundle
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

class HomeFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var nowPlayingFilmsAdapter: FilmsAdapter
    private lateinit var upcomingFilmsAdapter: FilmsAdapter

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

        return binding.root
    }

    private fun initNowPlayingFilmsRecycler() = with(binding) {
        recyclerViewNowPlaying.setHasFixedSize(true)
        nowPlayingFilmsAdapter = context?.let { FilmsAdapter(it) }!!
        nowPlayingFilmsAdapter.onDataRequiredListener = onDataRequiredListener
        nowPlayingFilmsAdapter.onFilmClickListener = onFilmClickListener
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
        upcomingFilmsAdapter = context?.let { FilmsAdapter(it) }!!
        upcomingFilmsAdapter.onDataRequiredListener = onDataRequiredListener
        upcomingFilmsAdapter.onFilmClickListener = onFilmClickListener
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
    }

    private fun observeNowPlayingFilms() {
        viewModel.listOfNowPlayingFilmsLiveData.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                nowPlayingFilmsAdapter.updateItems(it)

                if ((System.currentTimeMillis() - it[0].timeStamp).toInt() > FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS) {
                    viewModel.updateNowPlayingFilms(FIRST_PAGE_NUMBER)
                }
            } else {
                viewModel.updateNowPlayingFilms(FIRST_PAGE_NUMBER)
            }
        })
    }

    private fun observeUpcomingFilms() {
        viewModel.listOfUpcomingFilmsLiveData.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                upcomingFilmsAdapter.updateItems(it)

                if ((System.currentTimeMillis() - it[0].timeStamp).toInt() > FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS) {
                    viewModel.updateUpcomingFilms(FIRST_PAGE_NUMBER)
                }
            } else {
                viewModel.updateUpcomingFilms(FIRST_PAGE_NUMBER)
            }
        })
    }

    private fun observeLoadingFailure() {
        viewModel.failureLiveData.observe(viewLifecycleOwner, { failure ->
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
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nowPlayingFilmsAdapter.onDataRequiredListener = null
        nowPlayingFilmsAdapter.onFilmClickListener = null
        upcomingFilmsAdapter.onDataRequiredListener = null
        upcomingFilmsAdapter.onFilmClickListener = null
        _binding = null
    }

}
