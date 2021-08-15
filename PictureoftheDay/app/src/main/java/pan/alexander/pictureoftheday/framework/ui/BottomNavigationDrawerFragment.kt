package pan.alexander.pictureoftheday.framework.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.databinding.BottomNavigationLayoutBinding

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomNavigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigationOne -> findNavController().navigate(R.id.to_main_screen)
                R.id.navigationTwo -> {
                    activity?.let {
                        val intent = Intent(it, AnimationActivity::class.java)
                        val options = ActivityOptions
                            .makeSceneTransitionAnimation(
                                it,
                                it.findViewById(R.id.podImageView) as ImageView,
                                it.resources.getString(R.string.animation_activity_shared_transition)
                            )
                        startActivity(intent, options.toBundle())
                    }
                }
            }
            dismiss()
            true
        }
    }

}
