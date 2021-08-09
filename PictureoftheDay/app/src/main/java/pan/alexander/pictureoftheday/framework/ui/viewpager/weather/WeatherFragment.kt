package pan.alexander.pictureoftheday.framework.ui.viewpager.weather

import androidx.lifecycle.ViewModelProvider
import pan.alexander.pictureoftheday.framework.ui.viewpager.BaseViewPagerFragment

class WeatherFragment : BaseViewPagerFragment() {
    companion object {
        fun newInstance() = WeatherFragment()
    }

    private val weatherFragmentViewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }
}
