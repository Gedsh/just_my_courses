package pan.alexander.testweatherapp.domain

import kotlinx.coroutines.flow.Flow
import pan.alexander.testweatherapp.domain.entities.CurrentWeather

interface RemoteRepository {
    suspend fun getCurrentWeatherByZip(zipCode: Int): Flow<CurrentWeather>
}
