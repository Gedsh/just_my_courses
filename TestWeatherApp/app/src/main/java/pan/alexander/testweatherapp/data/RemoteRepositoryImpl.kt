package pan.alexander.testweatherapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import pan.alexander.testweatherapp.domain.RemoteRepository
import pan.alexander.testweatherapp.domain.entities.CurrentWeather
import pan.alexander.testweatherapp.utils.AppConstants.INTERVAL_REFRESH_WEATHER
import pan.alexander.testweatherapp.utils.exceptions.ResourceNotFoundException
import java.lang.RuntimeException

private const val RESOURCE_NOT_FOUND_CODE = 404

class RemoteRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: ServerResponseToCurrentWeatherMapper,
) : RemoteRepository {
    override suspend fun getCurrentWeatherByZip(zipCode: Int): Flow<CurrentWeather> {
        return flow {
            while (true) {
                remoteDataSource.getCurrentWeatherByZip(zipCode).let {
                    if (it.isSuccessful && it.body() != null) {
                        it.body()?.let { serverResponse ->
                            emit(mapper.map(zipCode, serverResponse))
                        }
                    } else if (it.code() == RESOURCE_NOT_FOUND_CODE) {
                        throw ResourceNotFoundException("Zip code not found")
                    } else {
                        it.errorBody()?.let { responseBody ->
                            loadErrorBody(responseBody)
                        }
                        throw RuntimeException("Unidentified error")
                    }
                }
                delay(INTERVAL_REFRESH_WEATHER)
            }
        }
    }

    private fun loadErrorBody(responseBody: ResponseBody) {
        kotlin.runCatching {
            responseBody.string()
        }.onSuccess { error ->
            if (error.isNotBlank()) {
                throw RuntimeException(error)
            }
        }.onFailure {
            throw RuntimeException(it.message)
        }
    }
}
