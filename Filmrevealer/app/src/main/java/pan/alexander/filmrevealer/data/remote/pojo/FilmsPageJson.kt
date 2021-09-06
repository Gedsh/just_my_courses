package pan.alexander.filmrevealer.data.remote.pojo

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FilmsPageJson(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<FilmDetailsJson>,
    @SerializedName("dates") val dateRange: DateRange?,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)
