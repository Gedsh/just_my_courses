package pan.alexander.filmrevealer.domain.remote

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import pan.alexander.filmrevealer.data.remote.pojo.ServerResponse
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import retrofit2.Response

interface RemoteRepository {
    fun getNowPlayingFilms(page: Int): Flowable<List<Film>>
    fun getUpcomingFilms(page: Int): Flowable<List<Film>>
    fun getTopRatedFilms(page: Int): Flowable<List<Film>>
    fun getPopularFilms(page: Int): Flowable<List<Film>>
    fun getUserRatedFilms(guestSessionId: String): Flowable<List<Film>>

    fun getFilmPreciseDetails(movieId: Int): Single<FilmDetails?>

    fun createGuestSession(): Single<String>

    fun rateFilm(
        movieId: Int,
        rate: Float,
        guestSessionId: String
    ): Single<Response<ServerResponse>>
}
