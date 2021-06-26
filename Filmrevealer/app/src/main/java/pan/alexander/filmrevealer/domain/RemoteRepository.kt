package pan.alexander.filmrevealer.domain

import pan.alexander.filmrevealer.data.web.pojo.FilmPreciseDetailsJson
import pan.alexander.filmrevealer.data.web.pojo.FilmsPageJson
import pan.alexander.filmrevealer.data.web.pojo.GuestSession
import pan.alexander.filmrevealer.data.web.pojo.ServerResponse
import retrofit2.Call

interface RemoteRepository {
    fun loadNowPlayingFilms(page: Int): Call<FilmsPageJson>
    fun loadUpcomingFilms(page: Int): Call<FilmsPageJson>
    fun loadTopRatedFilms(page: Int): Call<FilmsPageJson>
    fun loadPopularFilms(page: Int): Call<FilmsPageJson>
    fun loadFilmPreciseDetails(movieId: Int): Call<FilmPreciseDetailsJson>

    fun createGuestSession(): Call<GuestSession>
    fun getUserRatedFilms(guestSessionId: String): Call<FilmsPageJson>
    fun rateFilm(movieId: Int, rate: Float, guestSessionId: String): Call<ServerResponse>
}
