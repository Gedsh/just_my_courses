package pan.alexander.pictureoftheday.framework.ui.viewpager.earth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.framework.App.Companion.LOG_TAG
import pan.alexander.pictureoftheday.framework.ui.viewpager.BaseViewPagerFragment

class EarthFragment : BaseViewPagerFragment() {

    companion object {
        fun newInstance() = EarthFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(EarthViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEpicImageData()
    }

    private fun observeEpicImageData() {
        viewModel.getEpicLivaData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        viewModel.requestEpicRecentEnhancedColorImage()
    }

    private fun renderData(data: EpicActionData) {
        when (data) {
            is EpicActionData.Success -> {
                val epicImage = data.epicImage
                val url = epicImage.url
                if (url.isNullOrEmpty()) {
                    context?.let { showError(it.getString(R.string.error_empty_link)) }
                    hideLoadingIndicator()
                } else {
                    context?.let { loadImage(it, url) }
                }
            }
            is EpicActionData.Loading -> {
                showLoadingIndicator()
            }
            is EpicActionData.Error -> {
                hideLoadingIndicator()
                data.error.message?.let { showError(it) }
                Log.e(LOG_TAG, "Error while requesting EPIC image", data.error)
            }
        }
    }
}
