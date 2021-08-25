package pan.alexander.testweatherapp.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import pan.alexander.testweatherapp.domain.entities.CurrentWeather
import pan.alexander.testweatherapp.utils.AppConstants.ATTEMPTS_INTERVAL_TO_GET_WEATHER
import pan.alexander.testweatherapp.utils.AppConstants.ATTEMPTS_TO_GET_WEATHER
import pan.alexander.testweatherapp.utils.exceptions.ResourceNotFoundException
import java.io.IOException

class CurrentWeatherInteractorImpl(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : CurrentWeatherInteractor {
    override fun getCurrentWeatherFromDb(): Flow<List<CurrentWeather>> {
        return localRepository.getCurrentWeather()
    }

    override suspend fun requestCurrentWeather(zipCode: Int) {
        remoteRepository.getCurrentWeatherByZip(zipCode)
            .retry(ATTEMPTS_TO_GET_WEATHER) { throwable ->
                when (throwable) {
                    is ResourceNotFoundException -> false
                    is IOException -> {
                        delay(ATTEMPTS_INTERVAL_TO_GET_WEATHER)
                        true
                    }
                    else -> false
                }
            }
            .collectLatest {
                localRepository.saveCurrentWeather(it)
            }
    }
}
