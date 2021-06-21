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
import pan.alexander.filmrevealer.presentation.recycler.FilmsAdapter
import pan.alexander.filmrevealer.presentation.recycler.OnFilmClickListener
import pan.alexander.filmrevealer.presentation.recycler.OnRecyclerScrolledListener
import pan.alexander.filmrevealer.presentation.viewmodels.RatingsViewModel

class RatingsFragment : Fragment(), FilmsAdapter.OnDataRequiredListener, OnFilmClickListener {

    private lateinit var viewModel: RatingsViewModel
    private var _binding: FragmentRatingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var topRatedFilmsAdapter: FilmsAdapter
    private lateinit var popularFilmsAdapter: FilmsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(RatingsViewModel::class.java)

        _binding = FragmentRatingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initTopRatedFilmsRecycler()
        initPopularFilmsRecycler()

        return root
    }

    private fun initTopRatedFilmsRecycler() = with(binding) {
        recyclerViewTopRated.setHasFixedSize(true)
        topRatedFilmsAdapter = context?.let { FilmsAdapter(it) }!!
        topRatedFilmsAdapter.onDataRequiredListener = this@RatingsFragment
        topRatedFilmsAdapter.onFilmClickListener = this@RatingsFragment
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
        popularFilmsAdapter.onDataRequiredListener = this@RatingsFragment
        popularFilmsAdapter.onFilmClickListener = this@RatingsFragment
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

        binding.recyclerViewTopRated.requestFocus()

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

    override fun onDestroyView() {
        super.onDestroyView()
        topRatedFilmsAdapter.onDataRequiredListener = null
        topRatedFilmsAdapter.onFilmClickListener = null
        popularFilmsAdapter.onDataRequiredListener = null
        popularFilmsAdapter.onFilmClickListener = null
        _binding = null
    }

    override fun onDataRequired(section: Film.Section, page: Int) {
        when (section) {
            Film.Section.TOP_RATED -> viewModel.updateTopRatedFilms(page)
            Film.Section.POPULAR -> viewModel.updatePopularFilms(page)
            else -> Log.e(App.LOG_TAG, "Section $section is not supported in RatingsFragment")
        }
    }

    override fun onFilmClicked(view: View?, film: Film) {
        val bundle = bundleOf(FilmDetailsFragment.BUNDLE_EXTRA to film)
        view?.findNavController()?.navigate(R.id.navigation_to_film_details, bundle)
    }
}
