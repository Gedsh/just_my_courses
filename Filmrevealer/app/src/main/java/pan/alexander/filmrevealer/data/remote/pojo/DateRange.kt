package pan.alexander.filmrevealer.data.remote.pojo

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DateRange(
    @SerializedName("maximum") val maximum: String,
    @SerializedName("minimum") val minimum: String
)
