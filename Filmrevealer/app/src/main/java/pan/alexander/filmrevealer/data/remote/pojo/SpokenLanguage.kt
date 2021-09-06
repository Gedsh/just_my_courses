package pan.alexander.filmrevealer.data.remote.pojo

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SpokenLanguage(
    @SerializedName("iso_639_1") val iso_639_1: String,
    @SerializedName("name") val name: String
)
