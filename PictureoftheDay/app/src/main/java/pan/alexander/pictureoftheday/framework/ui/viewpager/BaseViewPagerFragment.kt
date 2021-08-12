package pan.alexander.pictureoftheday.framework.ui.viewpager

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
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

    protected fun loadImage(context: Context, url: String) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .lifecycle(this)
            .error(R.drawable.ic_load_error_vector)
            .placeholder(R.drawable.ic_no_photo_vector)
            .target(
                onSuccess = { result ->
                    hideLoadingIndicator()
                    binding.nasaImageView.setImageDrawable(result)
                    showInNasaImage()

                },
                onError = { error ->
                    hideLoadingIndicator()
                    binding.nasaImageView.setImageDrawable(error)
                    binding.nasaImageView.alpha = 1.0f
                }
            )
            .build()
        context.imageLoader.enqueue(request)
    }

    private fun showInNasaImage() {
        ObjectAnimator.ofFloat(binding.nasaImageView, "alpha", 1.0f).apply {
            duration = 1000
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
