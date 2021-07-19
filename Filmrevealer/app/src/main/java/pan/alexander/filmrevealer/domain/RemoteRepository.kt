package pan.alexander.filmrevealer.domain

import pan.alexander.filmrevealer.data.web.pojo.FilmPreciseDetailsJson
import pan.alexander.filmrevealer.data.web.pojo.FilmsPageJson
import pan.alexander.filmrevealer.data.web.pojo.GuestSession
import pan.alexander.filmrevealer.data.web.pojo.ServerResponse
import retrofit2.Response

interface RemoteRepository {
    suspend fun loadNowPlayingFilms(page: Int): Response<FilmsPageJson>
    suspend fun loadUpcomingFilms(page: Int): Response<FilmsPageJson>
    suspend fun loadTopRatedFilms(page: Int): Response<FilmsPageJson>
    suspend fun loadPopularFilms(page: Int): Response<FilmsPageJson>
    suspend fun loadFilmPreciseDetails(movieId: Int): Response<FilmPreciseDetailsJson>

    suspend fun createGuestSession(): Response<GuestSession>
    suspend fun getUserRatedFilms(guestSessionId: String): Response<FilmsPageJson>
    suspend fun rateFilm(
        movieId: Int,
        rate: Float,
        guestSessionId: String
    ): Response<ServerResponse>
}
