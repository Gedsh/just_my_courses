package pan.alexander.testweatherapp.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SystemDataApi(
    @SerializedName("type") val type: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("message") val message: Double,
    @SerializedName("country") val country: String,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long
)
