package pan.alexander.filmrevealer.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import pan.alexander.filmrevealer.databinding.MainFragmentBinding

private const val LOG_TAG = "filmrevealer"

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        initButtonOnClickListener()

        useDataClass()

        useSingletonClass()

        useLoops()
    }

    private fun initButtonOnClickListener() {
        binding.button.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun useDataClass() {
        val film = Film(1, "Some film")
        val filmCopy = film.copy(name = "Another film")
        film.id = -1

        binding.textViewFilmId.text = filmCopy.id.toString()
        binding.textViewFilmName.text = filmCopy.name
    }

    private fun useSingletonClass() {
        val one = Utils.getNumberOne()
        binding.textViewOne.text = one.toString()
    }

    private fun useLoops() {
        for(i in 1..10) {
            Log.d(LOG_TAG, i.toString())
        }

        for(i in 10 downTo 1 step 2) {
            Log.d(LOG_TAG, i.toString())
        }

        for (i in 0 until 10) {
            Log.d(LOG_TAG, i.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
