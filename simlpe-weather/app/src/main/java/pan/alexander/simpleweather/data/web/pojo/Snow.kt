package pan.alexander.simpleweather.data.web.pojo

import com.google.gson.annotations.SerializedName

data class Snow(
    @SerializedName("1h") val snowVolumeForHour: Int,
    @SerializedName("3h") val snowVolumeForThreeHours: Int
)