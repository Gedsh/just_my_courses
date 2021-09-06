package pan.alexander.testweatherapp.data

import androidx.annotation.AnyThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import pan.alexander.testweatherapp.domain.entities.CurrentWeather
import pan.alexander.testweatherapp.framework.database.CurrentWeatherDao
import kotlin.coroutines.CoroutineContext

class LocalDataSourceImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val dispatcherIO: CoroutineContext
) : LocalDataSource {
    override fun getCurrentWeather(): Flow<List<CurrentWeather>> {
        return currentWeatherDao.getCurrentWeather()
    }

    @AnyThread
    override suspend fun saveCurrentWeather(weather: CurrentWeather) {
        withContext(dispatcherIO) {
            currentWeatherDao.insertCurrentWeather(weather)
        }
    }
}
