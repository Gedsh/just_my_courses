package pan.alexander.pictureoftheday.framework.ui.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.databinding.ViewPagerCommonFragmentBinding
import pan.alexander.pictureoftheday.framework.ui.MainActivityViewModel

open class BaseViewPagerFragment : Fragment() {
    private val mainActivityViewModel by lazy {
        activity?.let {
            ViewModelProvider(it).get(MainActivityViewModel::class.java)
        }
    }

    private var _binding: ViewPagerCommonFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewPagerCommonFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    protected fun showLoadingIndicator() = with(binding) {
        imageLoadingProgressBar.visibility = View.VISIBLE
        nasaImageView.visibility = View.GONE
    }

    protected fun hideLoadingIndicator() = with(binding) {
        imageLoadingProgressBar.visibility = View.GONE
        nasaImageView.visibility = View.VISIBLE
    }

    protected fun showError(message: String) {
        mainActivityViewModel?.showErrorMessage(message)
    }

    protected fun loadImage(url: String) {
        binding.nasaImageView.load(url) {
            lifecycle(this@BaseViewPagerFragment)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.ic_no_photo_vector)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}