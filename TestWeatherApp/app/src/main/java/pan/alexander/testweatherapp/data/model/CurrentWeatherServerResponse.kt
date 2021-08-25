package pan.alexander.testweatherapp.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CurrentWeatherServerResponse(
    @SerializedName("weather") val weather: List<WeatherApi>,
    @SerializedName("main") val main: MainApi,
    @SerializedName("wind") val wind: WindApi,
    @SerializedName("sys") val systemData: SystemDataApi,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("timezone") val timeZoneShift: Long,
    @SerializedName("cod") val sysCode: Int
)
