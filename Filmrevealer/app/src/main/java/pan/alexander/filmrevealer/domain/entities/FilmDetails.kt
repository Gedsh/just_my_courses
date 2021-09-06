package pan.alexander.filmrevealer.domain.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import pan.alexander.filmrevealer.data.remote.pojo.FilmPreciseDetailsJson

@Keep
@Entity
data class FilmDetails(
    @PrimaryKey
    @ColumnInfo(name = "movie_id") var movieId: Int = 0,
    var title: String = "",
    @ColumnInfo(name = "original_title") var originalTitle: String = "",
    var genres: String = "",
    var runtime: Int? = 0,
    @ColumnInfo(name = "vote_average") var voteAverage: Float = .0f,
    @ColumnInfo(name = "vote_count") var voteCount: Int = 0,
    @Ignore var userRating: Int = 0,
    var budget: Int = 0,
    var revenue: Long = 0,
    @ColumnInfo(name = "release_date") var releaseDate: String = "",
    @ColumnInfo(name = "poster_url") var posterUrl: String = "",
    var overview: String? = "",
    var adult: Boolean = false,
    @ColumnInfo(name = "is_liked") var isLiked: Boolean = false,
    @ColumnInfo(name = "timestamp") var timeStamp: Long = System.currentTimeMillis()
) {
    constructor(filmPreciseDetailsJson: FilmPreciseDetailsJson) : this() {
        movieId = filmPreciseDetailsJson.id
        title = filmPreciseDetailsJson.title
        originalTitle = filmPreciseDetailsJson.originalTitle
        genres = filmPreciseDetailsJson.genres.joinToString(", ") { genre -> genre.name }
        runtime = filmPreciseDetailsJson.runtime ?: 0
        voteAverage = filmPreciseDetailsJson.voteAverage
        voteCount = filmPreciseDetailsJson.voteCount
        budget = filmPreciseDetailsJson.budget
        revenue = filmPreciseDetailsJson.revenue
        releaseDate = filmPreciseDetailsJson.releaseDate
        posterUrl = filmPreciseDetailsJson.posterPath ?: ""
        overview = filmPreciseDetailsJson.overview
        adult = filmPreciseDetailsJson.adult
    }
}
