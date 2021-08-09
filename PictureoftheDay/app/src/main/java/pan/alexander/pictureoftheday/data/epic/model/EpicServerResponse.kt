package pan.alexander.pictureoftheday.data.epic.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class EpicServerResponse(
    @SerializedName("image") val image: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("caption") val caption: String?
)
