package pan.alexander.filmrevealer.presentation.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import pan.alexander.filmrevealer.databinding.FragmentFavoritesBinding
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.recycler.FilmsAdapter
import pan.alexander.filmrevealer.presentation.viewmodels.FavoritesViewModel
import pan.alexander.filmrevealer.utils.InternetConnectionLiveData

class FavoritesFragment : FilmsBaseFragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(FavoritesViewModel::class.java) }
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var likedFilmsAdapter: FilmsAdapter? = null
    private var ratedFilmsAdapter: FilmsAdapter? = null

    private var likedRecycleViewState: Parcelable? = null
    private var ratedRecycleViewState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        initLikedFilmsRecycler()

        initRatedFilmsRecycler()

        likedRecycleViewState?.let {
            binding.recyclerViewLiked.layoutManager?.onRestoreInstanceState(it)
        }

        ratedRecycleViewState?.let {
            binding.recyclerViewRated.layoutManager?.onRestoreInstanceState(it)
        }

        return binding.root
    }

    private fun initLikedFilmsRecycler() = with(binding) {
        recyclerViewLiked.setHasFixedSize(true)
        likedFilmsAdapter = context?.let { FilmsAdapter(it) }?.also {
            it.setHasStableIds(true)
            it.onFilmClickListener = onFilmClickListener
            it.onLikeClickListener = onLikeClickListener
        }
        recyclerViewLiked.adapter = likedFilmsAdapter
    }

    private fun initRatedFilmsRecycler() = with(binding) {
        recyclerViewRated.setHasFixedSize(true)
        ratedFilmsAdapter = context?.let { FilmsAdapter(it) }?.also {
            it.setHasStableIds(true)
            it.onFilmClickListener = onFilmClickListener
            it.onLikeClickListener = onLikeClickListener
        }
        recyclerViewRated.adapter = ratedFilmsAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        binding.recyclerViewLiked.requestFocus()

        observeLikedFilmsImdbIds()

        observeLikedFilms()

        observeRatedFilms()

        observeLoadingFailure(viewModel, binding.root)

        observeInternetConnectionAvailable()
    }

    private fun observeLikedFilmsImdbIds() {
        viewModel.listOfLikedImdbIdsLiveData.distinctUntilChanged().observe(viewLifecycleOwner) {
            likedFilmsAdapter?.updateLikedImdbIds(it)
            ratedFilmsAdapter?.updateLikedImdbIds(it)
        }
    }

    private fun observeLikedFilms() {
        viewModel.listOfLikedFilmsLiveData.distinctUntilChanged()
            .observe(viewLifecycleOwner) { films ->

                if (films.isNotEmpty() && binding.textViewLiked.visibility == View.INVISIBLE) {
                    binding.textViewLiked.visibility = View.VISIBLE
                } else if (films.isEmpty() && binding.textViewLiked.visibility == View.VISIBLE) {
                    binding.textViewLiked.visibility = View.INVISIBLE
                }

                likedFilmsAdapter?.updateItems(films)
            }
    }

    private fun observeRatedFilms() {
        viewModel.listOfRatedFilmsLiveData.distinctUntilChanged()
            .observe(viewLifecycleOwner) { films ->
                if (films.isNotEmpty() && binding.textViewRated.visibility == View.INVISIBLE) {
                    binding.textViewRated.visibility = View.VISIBLE
                } else if (films.isEmpty() && binding.textViewRated.visibility == View.VISIBLE) {
                    binding.textViewRated.visibility = View.INVISIBLE
                }
                updateRatedFilms(films)
            }
    }

    private fun updateRatedFilms(films: List<Film>) {
        if (films.isNotEmpty()) {
            val recycleViewState =
                binding.recyclerViewRated.layoutManager?.onSaveInstanceState()

            ratedFilmsAdapter?.updateItems(films)

            if (isUpdateRequired(films.first().timeStamp)) {
                viewModel.updateRatedFilms()
            }

            binding.recyclerViewRated.layoutManager?.onRestoreInstanceState(
                recycleViewState
            )
        } else {
            viewModel.updateRatedFilms()
        }
    }

    private fun observeInternetConnectionAvailable() {
        context?.let {
            InternetConnectionLiveData.observe(viewLifecycleOwner) { connected ->
                if (connected) {
                    viewModel.listOfRatedFilmsLiveData.value?.let { updateRatedFilms(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        likedRecycleViewState = binding.recyclerViewLiked.layoutManager?.onSaveInstanceState()
        ratedRecycleViewState = binding.recyclerViewRated.layoutManager?.onSaveInstanceState()

        likedFilmsAdapter?.onFilmClickListener = null
        ratedFilmsAdapter?.onFilmClickListener = null

        _binding = null
    }
}
