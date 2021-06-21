package pan.alexander.filmrevealer.data.web.pojo

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProductionCountry(
    @SerializedName("iso_3166_1") val iso_3166_1: String,
    @SerializedName("name") val name: String
)
