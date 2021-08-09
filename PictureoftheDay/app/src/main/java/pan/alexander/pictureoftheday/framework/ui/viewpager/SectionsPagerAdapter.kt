package pan.alexander.pictureoftheday.framework.ui.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.framework.ui.viewpager.earth.EarthFragment
import pan.alexander.pictureoftheday.framework.ui.viewpager.mars.MarsFragment
import pan.alexander.pictureoftheday.framework.ui.viewpager.weather.WeatherFragment

private const val EARTH_FRAGMENT_POSITION = 0
private const val MARS_FRAGMENT_POSITION = 1
private const val WEATHER_FRAGMENT_POSITION = 2
private const val TABS_COUNT = 3

class SectionsPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return TABS_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            EARTH_FRAGMENT_POSITION -> EarthFragment.newInstance()
            MARS_FRAGMENT_POSITION -> MarsFragment.newInstance()
            WEATHER_FRAGMENT_POSITION -> WeatherFragment.newInstance()
            else -> throw IllegalStateException("No fragment for position $position")
        }
    }

    companion object {
        val TAB_TITLES = arrayOf(
            R.string.tab_text_earth,
            R.string.tab_text_mars,
            R.string.tab_text_weather
        )
    }
}
