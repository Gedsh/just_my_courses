package pan.alexander.simpleweather.data.web.pojo

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("temp_min") val min: Float,
    @SerializedName("temp_max") val max: Float,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("sea_level") val pressureSeaLevel: Int,
    @SerializedName("grnd_leve") val pressuregroundLevel: Int,
    @SerializedName("humidity") val humidity: Int,
)
