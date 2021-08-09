package pan.alexander.pictureoftheday.framework.ui.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import pan.alexander.pictureoftheday.databinding.FragmentViewPagerBinding

class PlaceholderFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        initViewPagerAdapter()

        return binding.root
    }

    private fun initViewPagerAdapter() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager2
        viewPager.adapter = sectionsPagerAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linkTabLayout()
    }

    private fun linkTabLayout() {
        TabLayoutMediator(binding.tabs, binding.viewPager2) { tab, position ->
            tab.text = getText(SectionsPagerAdapter.TAB_TITLES[position])
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
