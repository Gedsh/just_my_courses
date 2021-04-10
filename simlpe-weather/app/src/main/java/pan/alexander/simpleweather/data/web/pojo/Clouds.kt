package pan.alexander.simpleweather.data.web.pojo

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") val cloudiness: Int
)
