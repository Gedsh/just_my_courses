package pan.alexander.testweatherapp.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WeatherApi(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val condition: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val iconId: String
)
