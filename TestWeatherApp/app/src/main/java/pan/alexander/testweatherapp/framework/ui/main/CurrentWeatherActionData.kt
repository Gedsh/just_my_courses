package pan.alexander.testweatherapp.framework.ui.main

import pan.alexander.testweatherapp.domain.entities.CurrentWeather

sealed class CurrentWeatherActionData {
    data class Success(val currentWeather: CurrentWeather) : CurrentWeatherActionData()
    data class Error(val throwable: Throwable) : CurrentWeatherActionData()
    object Loading : CurrentWeatherActionData()
}
