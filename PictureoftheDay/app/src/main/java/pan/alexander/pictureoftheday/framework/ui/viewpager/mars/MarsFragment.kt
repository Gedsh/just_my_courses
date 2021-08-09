package pan.alexander.pictureoftheday.framework.ui.viewpager.mars

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.framework.App
import pan.alexander.pictureoftheday.framework.ui.viewpager.BaseViewPagerFragment

class MarsFragment : BaseViewPagerFragment() {
    companion object {
        fun newInstance() = MarsFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MarsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMarsPhotoData()
    }

    private fun observeMarsPhotoData() {
        viewModel.getMarsPhotoLivaData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        viewModel.requestCuriosityPhoto()
    }

    private fun renderData(data: MarsActionData) {
        when (data) {
            is MarsActionData.Success -> {
                hideLoadingIndicator()
                val marsPhoto = data.marsPhoto
                val url = marsPhoto.url
                if (url.isNullOrEmpty()) {
                    context?.let { showError(it.getString(R.string.error_empty_link)) }
                } else {
                    loadImage(url)
                }
            }
            is MarsActionData.Loading -> {
                showLoadingIndicator()
            }
            is MarsActionData.Error -> {
                hideLoadingIndicator()
                data.error.message?.let { showError(it) }
                Log.e(App.LOG_TAG, "Error while requesting Mars photo", data.error)
            }
        }
    }
}
