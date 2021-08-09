package pan.alexander.pictureoftheday.data.mars.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MarsPhotoApi(
    @SerializedName("img_src") val imageSource: String?,
    @SerializedName("rover") val rover: MarsRoverApi
)
