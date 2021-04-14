package pan.alexander.simpleweather.data.web.pojo

import com.google.gson.annotations.SerializedName

data class CurrentWeatherData(
    @SerializedName ("coord") val coordinates: Coordinates,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("base") val weatherSource: String,
    @SerializedName("main") val main: Main,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("rain") val rain: Rain?,
    @SerializedName("snow") val snow: Snow?,
    @SerializedName("dt") val unixTime: Long,
    @SerializedName("sys") val systemData: SystemData,
    @SerializedName("timezone") val timezone: Long,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val sysCode: Int
)
