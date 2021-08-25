package pan.alexander.testweatherapp.framework.web

import androidx.annotation.Keep
import pan.alexander.testweatherapp.data.model.CurrentWeatherServerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@Keep
interface CurrentWeatherApiService {
    @GET("data/2.5/weather?units=metric")
    suspend fun getCurrentWeatherByZip(
        @Query("zip") zipCode: Int,
        @Query("appid") apiKey: String
    ): Response<CurrentWeatherServerResponse>
}
