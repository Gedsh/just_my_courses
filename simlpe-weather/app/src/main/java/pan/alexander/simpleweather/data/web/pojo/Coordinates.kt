package pan.alexander.simpleweather.data.web.pojo

import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName("lon") val longitude: Float,
    @SerializedName("lat") val latitude: Float
)