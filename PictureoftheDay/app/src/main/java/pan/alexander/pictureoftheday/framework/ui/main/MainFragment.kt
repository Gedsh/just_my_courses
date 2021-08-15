package pan.alexander.pictureoftheday.framework.ui.main

import android.animation.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.imageLoader
import coil.request.ImageRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.databinding.MainFragmentBinding
import pan.alexander.pictureoftheday.databinding.MainFragmentBottomSheetBinding
import pan.alexander.pictureoftheday.domain.pod.entities.NasaPicture
import pan.alexander.pictureoftheday.framework.App.Companion.LOG_TAG
import pan.alexander.pictureoftheday.framework.ui.MainActivityViewModel
import java.util.*

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
    }

    override fun onStart() {
        super.onStart()

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
        bindingMainFragment.chipGroupDaySelection.setOnCheckedChangeListener { _, checkedId ->
            requestPicture(checkedId)
        }
    }

    private fun observePictureLiveData() {
        mainFragmentViewModel.getPictureLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        requestPicture(bindingMainFragment.chipGroupDaySelection.checkedChipId)
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
                val nasaPicture = data.nasaPicture
                val url = nasaPicture.url
                if (url.isNullOrEmpty()) {
                    context?.let { showError(it.getString(R.string.error_empty_link)) }
                    hideLoadingIndicator()
                } else {
                    fillBottomSheet(nasaPicture)
                    context?.let { loadImage(it, url) }
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

    private fun loadImage(context: Context, url: String) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .lifecycle(this@MainFragment)
            .error(R.drawable.ic_load_error_vector)
            .placeholder(R.drawable.ic_no_photo_vector)
            .target(
                onSuccess = { result ->
                    hideLoadingIndicator()
                    bindingMainFragment.podImageView.setImageDrawable(result)
                    showInPodImage(context)
                    expandImageOnClick()
                },
                onError = { error ->
                    hideLoadingIndicator()
                    bindingMainFragment.podImageView.setImageDrawable(error)
                    bindingMainFragment.podImageView.alpha = 1.0f
                }
            )
            .build()
        context.imageLoader.enqueue(request)
    }

    private fun showInPodImage(context: Context) {
        AnimatorInflater.loadAnimator(context, R.animator.pod_image_animate)
            .apply {
                setTarget(bindingMainFragment.podImageView)
                start()
            }
    }

    private fun expandImageOnClick() {
        val animation = animateImageOnClick()
        bindingMainFragment.podImageView.setOnClickListener {
            animation()
        }
    }

    private fun animateImageOnClick(): () -> Unit {
        var expanded = false

        return {
            expanded = !expanded
            TransitionManager.beginDelayedTransition(
                bindingMainFragment.root, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            if (expanded) {
                bindingMainFragment.podImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                expandImageUsingConstraints(R.id.start)
                expandImageUsingConstraints(R.id.end)
            } else {
                bindingMainFragment.podImageView.scaleType = ImageView.ScaleType.FIT_CENTER
                collapseImageUsingConstraints(R.id.start)
                collapseImageUsingConstraints(R.id.end)
            }
        }
    }

    private fun expandImageUsingConstraints(stateId: Int) {
        bindingMainFragment.mainConstraint.getConstraintSet(stateId).apply {
            constrainHeight(R.id.podImageView, ConstraintSet.MATCH_CONSTRAINT)
            constrainPercentWidth(R.id.podImageView, 1f)
            connect(
                R.id.podImageView,
                ConstraintSet.TOP,
                R.id.mainConstraint,
                ConstraintSet.TOP
            )
        }
    }

    private fun collapseImageUsingConstraints(stateId: Int) {
        bindingMainFragment.mainConstraint.getConstraintSet(stateId).apply {
            constrainHeight(R.id.podImageView, ConstraintSet.WRAP_CONTENT)
            constrainPercentWidth(R.id.podImageView, .9f)
            connect(
                R.id.podImageView,
                ConstraintSet.TOP,
                R.id.chipGroupDaySelection,
                ConstraintSet.BOTTOM
            )
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

    override fun onDestroyView() {
        super.onDestroyView()

        _bindingBottomSheet = null
        _bindingMainFragment = null
    }
}
