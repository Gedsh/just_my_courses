package pan.alexander.testweatherapp.data

import kotlinx.coroutines.flow.Flow
import pan.alexander.testweatherapp.domain.entities.CurrentWeather
import pan.alexander.testweatherapp.domain.LocalRepository

class LocalRepositoryImpl(
    private val localDataSource: LocalDataSource
) : LocalRepository {
    override fun getCurrentWeather(): Flow<List<CurrentWeather>> {
        return localDataSource.getCurrentWeather()
    }

    override suspend fun saveCurrentWeather(weather: CurrentWeather) {
        return localDataSource.saveCurrentWeather(weather)
    }
}
