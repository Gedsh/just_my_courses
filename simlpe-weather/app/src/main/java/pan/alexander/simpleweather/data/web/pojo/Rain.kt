package pan.alexander.simpleweather.data.web.pojo

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h") val rainVolumeForHour: Int,
    @SerializedName("3h") val rainVolumeForThreeHours: Int
)
