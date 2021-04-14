package pan.alexander.simpleweather.domain

import androidx.lifecycle.LiveData
import pan.alexander.simpleweather.domain.entities.CurrentWeather
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository
) {
    fun getCurrentWeather(): LiveData<List<CurrentWeather>> {
        return currentWeatherRepository.getCurrentWeatherFromDB()
    }
}
