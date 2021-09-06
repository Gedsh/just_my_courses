package pan.alexander.testweatherapp.data

import androidx.annotation.AnyThread
import pan.alexander.testweatherapp.data.model.CurrentWeatherServerResponse
import retrofit2.Response

interface RemoteDataSource {
    @AnyThread
    suspend fun getCurrentWeatherByZip(zipCode: Int): Response<CurrentWeatherServerResponse>
}
