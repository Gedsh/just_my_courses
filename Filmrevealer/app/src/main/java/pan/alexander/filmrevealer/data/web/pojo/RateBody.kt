package pan.alexander.filmrevealer.data.web.pojo

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RateBody(
    @SerializedName("value") val rate: Float
)
