package pan.alexander.simpleweather.data.web

import pan.alexander.simpleweather.data.web.pojo.CurrentWeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeatherApiService {
    @GET("/data/2.5/weather?")
    fun getWeatherForCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): Call<CurrentWeatherData>
}
