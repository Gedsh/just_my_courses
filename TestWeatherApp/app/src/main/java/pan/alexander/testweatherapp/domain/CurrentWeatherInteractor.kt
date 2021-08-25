package pan.alexander.testweatherapp.domain

import kotlinx.coroutines.flow.Flow
import pan.alexander.testweatherapp.domain.entities.CurrentWeather

interface CurrentWeatherInteractor {
    fun getCurrentWeatherFromDb(): Flow<List<CurrentWeather>>

    suspend fun requestCurrentWeather(zipCode: Int)
}
