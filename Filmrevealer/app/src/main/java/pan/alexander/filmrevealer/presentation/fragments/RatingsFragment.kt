package pan.alexander.filmrevealer.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pan.alexander.filmrevealer.databinding.FragmentRatingsBinding
import pan.alexander.filmrevealer.presentation.viewmodels.RatingsViewModel

class RatingsFragment : Fragment() {

    private lateinit var ratingsViewModel: RatingsViewModel
    private var _binding: FragmentRatingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ratingsViewModel =
            ViewModelProvider(this).get(RatingsViewModel::class.java)

        _binding = FragmentRatingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        ratingsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
