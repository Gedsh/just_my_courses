package pan.alexander.simpleweather.presentation.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import pan.alexander.simpleweather.databinding.MainFragmentBinding
import pan.alexander.simpleweather.domain.entities.CurrentWeather
import pan.alexander.simpleweather.presentation.viewmodel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)

        val currentWeatherObservable = Observer<List<CurrentWeather>> { newWeather ->
            if (newWeather.isEmpty()) {
                viewModel.updateCurrentWeather(binding)
            } else {
                viewModel.setWeatherFields(newWeather[0], binding)
            }
        }

        viewModel.currentWeather.observe(viewLifecycleOwner, currentWeatherObservable)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
