package pan.alexander.testweatherapp.data

import androidx.annotation.AnyThread
import kotlinx.coroutines.flow.Flow
import pan.alexander.testweatherapp.domain.entities.CurrentWeather

interface LocalDataSource {
    fun getCurrentWeather(): Flow<List<CurrentWeather>>

    @AnyThread
    suspend fun saveCurrentWeather(weather: CurrentWeather)
}
