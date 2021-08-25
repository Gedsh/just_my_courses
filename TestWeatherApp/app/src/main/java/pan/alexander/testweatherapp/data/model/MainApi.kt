package pan.alexander.testweatherapp.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MainApi(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
)
