package pan.alexander.filmrevealer.data.web.pojo

import com.google.gson.annotations.SerializedName

data class DateRange(
    @SerializedName("maximum") val maximum: String,
    @SerializedName("minimum") val minimum: String
)
