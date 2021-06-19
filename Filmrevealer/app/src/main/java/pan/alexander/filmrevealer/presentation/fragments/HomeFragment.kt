package pan.alexander.filmrevealer.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pan.alexander.filmrevealer.databinding.FragmentHomeBinding
import pan.alexander.filmrevealer.presentation.recycler.FilmsAdapter
import pan.alexander.filmrevealer.presentation.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var nowPlayingFilmsAdapter: FilmsAdapter
    private lateinit var upcomingFilmsAdapter: FilmsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclers()

        return root
    }

    private fun initRecyclers() = with(binding) {
        nowPlayingFilmsAdapter = context?.let { FilmsAdapter(it) }!!

        recyclerViewNowPlaying.adapter = nowPlayingFilmsAdapter

        upcomingFilmsAdapter = context?.let { FilmsAdapter(it) }!!

        recyclerViewUpcoming.adapter = upcomingFilmsAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listOfNowPlayingFilmsLiveData.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                nowPlayingFilmsAdapter.updateItems(it)
            }
        })

        viewModel.listOfUpcomingFilmsLiveData.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                upcomingFilmsAdapter.updateItems(it)
            }
        })

        viewModel.updateNowPlayingFilms(1)
        viewModel.updateUpcomingFilms(1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
