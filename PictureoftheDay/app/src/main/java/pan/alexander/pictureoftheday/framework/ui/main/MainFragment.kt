package pan.alexander.pictureoftheday.framework.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.databinding.MainFragmentBinding
import pan.alexander.pictureoftheday.databinding.MainFragmentBottomSheetBinding
import pan.alexander.pictureoftheday.domain.picture.entities.NasaPicture
import pan.alexander.pictureoftheday.framework.App.Companion.LOG_TAG
import pan.alexander.pictureoftheday.framework.ui.MainActivityViewModel
import java.util.*

private const val DELAY_BEFORE_LISTENING_CHIPS_CHECKING_CHANGE = 1000L
private const val DELAY_BEFORE_REQUESTING_PICTURE_ON_APP_START = 500L

class MainFragment : Fragment() {

    private val mainFragmentViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val mainActivityViewModel by lazy {
        activity?.let {
            ViewModelProvider(it).get(MainActivityViewModel::class.java)
        }
    }
    private var _bindingMainFragment: MainFragmentBinding? = null
    private val bindingMainFragment get() = _bindingMainFragment!!
    private var _bindingBottomSheet: MainFragmentBottomSheetBinding? = null
    private val bindingBottomSheet get() = _bindingBottomSheet!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingMainFragment = MainFragmentBinding.inflate(inflater, container, false)
        _bindingBottomSheet = MainFragmentBottomSheetBinding
            .bind(bindingMainFragment.root.findViewById(R.id.bottom_sheet_container))
        return bindingMainFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        initWikiSearchListener()

        initPictureDaySelectionListener()

        observePictureLiveData()
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun initWikiSearchListener() {
        bindingMainFragment.wikiSearchInputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://en.wikipedia.org/wiki/${bindingMainFragment.wikiSearchInputEditText.text.toString()}"
                )
            })
        }
    }

    private fun initPictureDaySelectionListener() {
        bindingMainFragment.chipGroupDaySelection.postDelayed({
            bindingMainFragment.chipGroupDaySelection.setOnCheckedChangeListener { _, checkedId ->
                requestPicture(checkedId)
            }
        }, DELAY_BEFORE_LISTENING_CHIPS_CHECKING_CHANGE)
    }

    private fun observePictureLiveData() {
        mainFragmentViewModel.getPictureLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        bindingMainFragment.chipGroupDaySelection.postDelayed({
            requestPicture(bindingMainFragment.chipGroupDaySelection.checkedChipId)
        }, DELAY_BEFORE_REQUESTING_PICTURE_ON_APP_START)

    }

    private fun requestPicture(checkedChipId: Int) {
        when (checkedChipId) {
            R.id.chipToday -> {
                mainFragmentViewModel.requestPictureOfTheDay(Date())
            }
            R.id.chipYesterday -> {
                mainFragmentViewModel.requestPictureOfTheDay(
                    Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
                )
            }
            R.id.chipBeforeYesterday -> {
                mainFragmentViewModel.requestPictureOfTheDay(
                    Calendar.getInstance().apply { add(Calendar.DATE, -2) }.time
                )
            }
        }
    }

    private fun renderData(data: PictureActionData) {
        when (data) {
            is PictureActionData.Success -> {
                hideLoadingIndicator()
                val nasaPicture = data.nasaPicture
                val url = nasaPicture.url
                if (url.isNullOrEmpty()) {
                    context?.let { showError(it.getString(R.string.error_empty_link)) }
                } else {
                    fillBottomSheet(nasaPicture)
                    loadImage(url)
                }
            }
            is PictureActionData.Loading -> {
                showLoadingIndicator()
            }
            is PictureActionData.Error -> {
                hideLoadingIndicator()
                data.error.message?.let { showError(it) }
                Log.e(LOG_TAG, "Error while requesting picture", data.error)
            }
        }
    }

    private fun fillBottomSheet(nasaPicture: NasaPicture) = with(bindingBottomSheet) {
        bindingBottomSheet.bottomSheetDescriptionHeader.text = nasaPicture.title
        bindingBottomSheet.bottomSheetDescription.text = nasaPicture.explanation
        bindingBottomSheet.root.visibility = View.VISIBLE
    }

    private fun loadImage(url: String) {
        bindingMainFragment.podImageView.load(url) {
            lifecycle(this@MainFragment)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.ic_no_photo_vector)
        }
    }

    private fun showLoadingIndicator() = with(bindingMainFragment) {
        podLoadingProgressBar.visibility = View.VISIBLE
        podImageView.visibility = View.GONE
    }

    private fun hideLoadingIndicator() = with(bindingMainFragment) {
        podLoadingProgressBar.visibility = View.GONE
        podImageView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        mainActivityViewModel?.showErrorMessage(message)
    }
}
