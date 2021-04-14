package pan.alexander.simpleweather.presentation.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import pan.alexander.simpleweather.App
import pan.alexander.simpleweather.R
import pan.alexander.simpleweather.databinding.MainFragmentBinding
import pan.alexander.simpleweather.domain.entities.CurrentWeather

private const val UPDATE_INTERVAL = 5 //MINUTES

class MainViewModel : ViewModel() {
    private val mainInteractor by lazy { App.instance.daggerComponent.getInteractor() }
    private val repository by lazy { App.instance.daggerComponent.getRepository() }
    private val appContext by lazy { App.instance.daggerComponent.getAppContext() }

    val currentWeather = mainInteractor.getCurrentWeather()

    fun updateCurrentWeather(binding: MainFragmentBinding) {
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.isIndeterminate = true
        repository.loadCurrentWeatherData(App.instance.applicationContext)
    }

    fun setWeatherFields(newWeather: CurrentWeather?, binding: MainFragmentBinding) {

        newWeather ?: return

        binding.tvLocation.text = String.format("%s, %s", newWeather.country, newWeather.city)
        binding.tvCondition.text = newWeather.currentCondition
        binding.tvDescription.text = newWeather.conditionDescription
        binding.tvCloudiness.text = newWeather.cloudiness.toString()
        binding.tvCurrentTemperature.text = newWeather.temperature.toString()
        binding.tvFeelsLike.text = newWeather.feelsLike.toString()
        binding.tvPressure.text = newWeather.pressure.toString()
        binding.tvHumidity.text = newWeather.humidity.toString()
        binding.tvWindSpeed.text = newWeather.windSpeed.toString()
        binding.tvWindDirection.text = newWeather.windDirection.toString()


        if (newWeather.rain > 0) {
            binding.tvRain.visibility = View.VISIBLE
            binding.tvRainTitle.visibility = View.VISIBLE
            binding.tvRain.text = newWeather.rain.toString()
        } else {
            binding.tvRain.visibility = View.GONE
            binding.tvRainTitle.visibility = View.GONE
        }

        if (newWeather.snow > 0) {
            binding.tvSnowTitle.visibility = View.VISIBLE
            binding.tvSnow.visibility = View.VISIBLE
            binding.tvSnow.text = newWeather.snow.toString()
        } else {
            binding.tvSnowTitle.visibility = View.GONE
            binding.tvSnow.visibility = View.GONE
        }

        if (newWeather.dataSource.isBlank()) {
            binding.tvDataSourceTitle.visibility = View.GONE
            binding.tvDataSource.visibility = View.GONE
        } else {
            binding.tvDataSourceTitle.visibility = View.VISIBLE
            binding.tvDataSource.visibility = View.VISIBLE
            binding.tvDataSource.text = newWeather.dataSource
        }

        if (newWeather.iconCode.isNotBlank()) {
            updateIcon(appContext, binding, newWeather.iconCode)
        }

        if (System.currentTimeMillis() - newWeather.entryTime > UPDATE_INTERVAL * 60 * 1000) {
            updateCurrentWeather(binding)
        } else {
            binding.progressBar.isIndeterminate = false
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun updateIcon(context: Context, binding: MainFragmentBinding, imageCode: String) {
        Glide.with(context)
            .load(App.BASE_IMAGE_URL + "img/wn/" + imageCode + "@2x.png")
            .placeholder(R.drawable.ic_baseline_cached_24)
            .error(R.drawable.ic_baseline_bug_report_24)
            .into(binding.ivWeatherIcon)
    }
}
