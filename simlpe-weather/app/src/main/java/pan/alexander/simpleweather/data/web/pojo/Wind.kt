package pan.alexander.simpleweather.data.web.pojo

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed") val speed: Float,
    @SerializedName("deg") val deg: Int,
    @SerializedName("gust") val gust: Float,
)
