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
import pan.alexander.filmrevealer.databinding.FragmentRatingsBinding
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.Failure
import pan.alexander.filmrevealer.presentation.recycler.FilmsAdapter
import pan.alexander.filmrevealer.presentation.recycler.OnRecyclerScrolledListener
import pan.alexander.filmrevealer.presentation.viewmodels.RatingsViewModel

class RatingsFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(RatingsViewModel::class.java) }
    private var _binding: FragmentRatingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var topRatedFilmsAdapter: FilmsAdapter
    private lateinit var popularFilmsAdapter: FilmsAdapter

    private val onDataRequiredListener by lazy {
        fun(section: Film.Section, page: Int) {
            when (section) {
                Film.Section.TOP_RATED -> viewModel.updateTopRatedFilms(page)
                Film.Section.POPULAR -> viewModel.updatePopularFilms(page)
                else -> Log.e(App.LOG_TAG, "Section $section is not supported in RatingsFragment")
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
        _binding = FragmentRatingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initTopRatedFilmsRecycler()
        initPopularFilmsRecycler()

        return root
    }

    private fun initTopRatedFilmsRecycler() = with(binding) {
        recyclerViewTopRated.setHasFixedSize(true)
        topRatedFilmsAdapter = context?.let { FilmsAdapter(it) }!!
        topRatedFilmsAdapter.onDataRequiredListener = onDataRequiredListener
        topRatedFilmsAdapter.onFilmClickListener = onFilmClickListener
        recyclerViewTopRated.adapter = topRatedFilmsAdapter

        val layoutManager = recyclerViewTopRated.layoutManager as? LinearLayoutManager
        val recyclerScrolledListener = topRatedFilmsAdapter as? OnRecyclerScrolledListener
        recyclerViewTopRated.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun initPopularFilmsRecycler() = with(binding) {
        recyclerViewPopular.setHasFixedSize(true)
        popularFilmsAdapter = context?.let { FilmsAdapter(it) }!!
        popularFilmsAdapter.onDataRequiredListener = onDataRequiredListener
        popularFilmsAdapter.onFilmClickListener = onFilmClickListener
        recyclerViewPopular.adapter = popularFilmsAdapter

        val layoutManager = recyclerViewPopular.layoutManager as? LinearLayoutManager
        val recyclerScrolledListener = popularFilmsAdapter as? OnRecyclerScrolledListener
        recyclerViewPopular.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        binding.recyclerViewTopRated.requestFocus()

        observeTopRatedFilms()

        observePopularFilms()

        observeLoadingFailure()
    }

    private fun observeTopRatedFilms() {
        viewModel.listOfTopRatedFilmsLiveData.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                topRatedFilmsAdapter.updateItems(it)

                if ((System.currentTimeMillis() - it[0].timeStamp).toInt() > FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS) {
                    viewModel.updateTopRatedFilms(FIRST_PAGE_NUMBER)
                }
            } else {
                viewModel.updateTopRatedFilms(FIRST_PAGE_NUMBER)
            }
        })
    }

    private fun observePopularFilms() {
        viewModel.listOfPopularFilmsLiveData.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                popularFilmsAdapter.updateItems(it)

                if ((System.currentTimeMillis() - it[0].timeStamp).toInt() > FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS) {
                    viewModel.updatePopularFilms(FIRST_PAGE_NUMBER)
                }
            } else {
                viewModel.updatePopularFilms(FIRST_PAGE_NUMBER)
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
        topRatedFilmsAdapter.onDataRequiredListener = null
        topRatedFilmsAdapter.onFilmClickListener = null
        popularFilmsAdapter.onDataRequiredListener = null
        popularFilmsAdapter.onFilmClickListener = null
        _binding = null
    }
}
