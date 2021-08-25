package pan.alexander.testweatherapp.domain

import kotlinx.coroutines.flow.Flow
import pan.alexander.testweatherapp.domain.entities.CurrentWeather

interface LocalRepository {
    fun getCurrentWeather(): Flow<List<CurrentWeather>>

    suspend fun saveCurrentWeather(weather: CurrentWeather)
}
