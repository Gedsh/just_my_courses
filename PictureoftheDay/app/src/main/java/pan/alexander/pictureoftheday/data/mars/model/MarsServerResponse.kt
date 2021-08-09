package pan.alexander.pictureoftheday.data.mars.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MarsServerResponse(
    @SerializedName("photos") val photos: List<MarsPhotoApi>
)
