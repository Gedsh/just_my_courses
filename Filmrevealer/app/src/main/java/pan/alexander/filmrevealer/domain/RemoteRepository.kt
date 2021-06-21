package pan.alexander.filmrevealer.domain

import pan.alexander.filmrevealer.data.web.pojo.FilmPreciseDetailsJson
import pan.alexander.filmrevealer.data.web.pojo.FilmsPageJson
import retrofit2.Call

interface RemoteRepository {
    fun loadNowPlayingFilms(page: Int): Call<FilmsPageJson>
    fun loadUpcomingFilms(page: Int): Call<FilmsPageJson>
    fun loadTopRatedFilms(page: Int): Call<FilmsPageJson>
    fun loadPopularFilms(page: Int): Call<FilmsPageJson>
    fun loadFilmPreciseDetails(movieId: Int): Call<FilmPreciseDetailsJson>
}
