package pan.alexander.simpleweather.domain

import pan.alexander.simpleweather.App
import pan.alexander.simpleweather.domain.entities.CurrentWeather
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository
) {
    private val context = App.instance.applicationContext

    fun getCurrentWeather(): CurrentWeather {
        return currentWeatherRepository.getCurrentWeatherFromDB()
    }
}