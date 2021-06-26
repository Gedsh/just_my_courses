package pan.alexander.filmrevealer.domain.entities

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import pan.alexander.filmrevealer.data.web.pojo.FilmDetailsJson

@Keep
@Parcelize
@Entity(indices = [Index("title")])
data class Film(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "movie_id") var movieId: Int = 0,
    var title: String = "",
    @ColumnInfo(name = "poster_url") var posterUrl: String = "",
    @ColumnInfo(name = "release_date") var releaseDate: String = "",
    var voteAverage: Float = .0f,
    @ColumnInfo(name = "user_rating") var userRating: Int = 0,
    @ColumnInfo(name = "adult") var adult: Boolean = false,
    @ColumnInfo(name = "is_liked") var isLiked: Boolean = false,
    @ColumnInfo(name = "section") var section: Int = Section.NOW_PLAYING.value,
    var page: Int = 0,
    @ColumnInfo(name = "total_pages") var totalPages: Int = 0,
    @ColumnInfo(name = "timestamp") var timeStamp: Long = System.currentTimeMillis()
) : Parcelable {
    constructor(filmDetailsJson: FilmDetailsJson) : this() {
        movieId = filmDetailsJson.id
        title = filmDetailsJson.title
        posterUrl = filmDetailsJson.posterPath ?: ""
        releaseDate = filmDetailsJson.releaseDate
        voteAverage = filmDetailsJson.voteAverage
        userRating = filmDetailsJson.rating ?: 0
        adult = filmDetailsJson.adult
    }

    enum class Section(val value: Int) {
        NOW_PLAYING(1),
        POPULAR(2),
        TOP_RATED(3),
        UPCOMING(4),
        USER_RATED(5),
        LIKED(6)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Film

        if (movieId != other.movieId) return false
        if (title != other.title) return false
        if (posterUrl != other.posterUrl) return false
        if (releaseDate != other.releaseDate) return false
        if (voteAverage != other.voteAverage) return false
        if (adult != other.adult) return false
        if (isLiked != other.isLiked) return false
        if (section != other.section) return false
        if (page != other.page) return false
        if (totalPages != other.totalPages) return false

        return true
    }

    override fun hashCode(): Int {
        var result = movieId
        result = 31 * result + title.hashCode()
        result = 31 * result + posterUrl.hashCode()
        result = 31 * result + releaseDate.hashCode()
        result = 31 * result + voteAverage.hashCode()
        result = 31 * result + adult.hashCode()
        result = 31 * result + isLiked.hashCode()
        result = 31 * result + section
        result = 31 * result + page
        result = 31 * result + totalPages
        return result
    }

    override fun toString(): String {
        return "Film(id=$id, movieId=$movieId, title='$title', posterUrl='$posterUrl'," +
                " releaseDate='$releaseDate', voteAverage=$voteAverage, userRating=$userRating," +
                " adult=$adult, isLiked=$isLiked, section=$section, page=$page," +
                " totalPages=$totalPages, timeStamp=$timeStamp)"
    }

}
