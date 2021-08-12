package pan.alexander.pictureoftheday.data.picture.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ApiServerResponse(
    @SerializedName("copyright") val copyright: String?,
    @SerializedName("date") val dateString: String?,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("hdurl") val hdUrl: String?
)
