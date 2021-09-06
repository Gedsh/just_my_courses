package pan.alexander.filmrevealer.data.remote

import io.reactivex.rxjava3.core.Single
import pan.alexander.filmrevealer.data.remote.pojo.FilmPreciseDetailsJson
import pan.alexander.filmrevealer.data.remote.pojo.FilmsPageJson
import pan.alexander.filmrevealer.data.remote.pojo.GuestSession
import pan.alexander.filmrevealer.data.remote.pojo.ServerResponse
import retrofit2.Response

interface RemoteDataSource {
    fun loadNowPlayingFilms(page: Int): Single<Response<FilmsPageJson>>
    fun loadUpcomingFilms(page: Int): Single<Response<FilmsPageJson>>
    fun loadTopRatedFilms(page: Int): Single<Response<FilmsPageJson>>
    fun loadPopularFilms(page: Int): Single<Response<FilmsPageJson>>
    fun loadFilmPreciseDetails(movieId: Int): Single<Response<FilmPreciseDetailsJson>>

    fun createGuestSession(): Single<Response<GuestSession>>
    fun getUserRatedFilms(guestSessionId: String): Single<Response<FilmsPageJson>>
    fun rateFilm(
        movieId: Int,
        rate: Float,
        guestSessionId: String
    ): Single<Response<ServerResponse>>
}
