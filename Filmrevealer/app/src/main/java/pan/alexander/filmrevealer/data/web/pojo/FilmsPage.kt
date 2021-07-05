package pan.alexander.filmrevealer.data.web.pojo

import com.google.gson.annotations.SerializedName

data class FilmsPage(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<FilmDetails>,
    @SerializedName("dates") val dateRange: DateRange,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)
