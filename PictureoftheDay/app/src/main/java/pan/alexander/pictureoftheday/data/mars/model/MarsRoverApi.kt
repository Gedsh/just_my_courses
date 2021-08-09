package pan.alexander.pictureoftheday.data.mars.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MarsRoverApi(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("landing_date") val landingDate: String?,
    @SerializedName("launch_date") val launchDate: String?,
    @SerializedName("status") val status: String?
)
