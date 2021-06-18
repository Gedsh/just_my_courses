package pan.alexander.filmrevealer.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import pan.alexander.filmrevealer.data.web.pojo.FilmDetails

@Entity(indices = [Index("title")])
data class Film(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "movie_id") var movieId: Int = 0,
    var title: String = "",
    @ColumnInfo(name = "poster_url") var posterUrl: String = "",
    @ColumnInfo(name = "release_date") var releaseDate: String = "",
    var rating: Float = .0f,
    @ColumnInfo(name = "adult") var adult: Boolean = false,
    @ColumnInfo(name = "is_liked") var isLiked: Boolean = false,
    @ColumnInfo(name = "section") var section: Section = Section.NOW_PLAYING
) {
    constructor(filmDetails: FilmDetails) : this() {
        movieId = filmDetails.id
        title = filmDetails.title
        posterUrl = filmDetails.posterPath ?: ""
        releaseDate = filmDetails.releaseDate
        rating = filmDetails.voteAverage
        adult = filmDetails.adult
    }

    enum class Section(val value: Int) {
        NOW_PLAYING(1),
        POPULAR(2),
        TOP_RATED(3),
        UPCOMING(4)
    }
}
