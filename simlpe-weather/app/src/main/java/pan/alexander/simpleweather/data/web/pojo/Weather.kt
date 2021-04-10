package pan.alexander.simpleweather.data.web.pojo

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val condition: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val iconId: String
)
