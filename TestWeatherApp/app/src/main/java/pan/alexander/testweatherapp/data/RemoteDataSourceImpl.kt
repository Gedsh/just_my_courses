package pan.alexander.testweatherapp.data

import androidx.annotation.AnyThread
import kotlinx.coroutines.withContext
import pan.alexander.testweatherapp.data.model.CurrentWeatherServerResponse
import pan.alexander.testweatherapp.framework.web.CurrentWeatherApiService
import pan.alexander.testweatherapp.utils.configuration.ConfigurationManager
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class RemoteDataSourceImpl(
    private val currentWeatherApiService: CurrentWeatherApiService,
    private val configurationManager: ConfigurationManager,
    private val dispatcherIO: CoroutineContext
) : RemoteDataSource {
    @AnyThread
    override suspend fun getCurrentWeatherByZip(zipCode: Int): Response<CurrentWeatherServerResponse> {
        return withContext(dispatcherIO) {
            currentWeatherApiService.getCurrentWeatherByZip(
                zipCode,
                configurationManager.getApiKey()
            )
        }
    }
}
