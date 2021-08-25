package pan.alexander.testweatherapp.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WindApi(
    @SerializedName("speed") val speed: Float
)
