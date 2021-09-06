package pan.alexander.filmrevealer.data.remote.pojo

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
