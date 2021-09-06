package pan.alexander.filmrevealer.domain.interactors

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails

interface MainInteractor {
    fun loadNowPlayingFilms(page: Int): Completable
    fun loadUpcomingFilms(page: Int): Completable
    fun loadTopRatedFilms(page: Int): Completable
    fun loadPopularFilms(page: Int): Completable
    fun loadUserRatedFilms(guestSessionId: String): Completable
    fun loadFilmPreciseDetails(movieId: Int): Completable
    @ExperimentalCoroutinesApi
    fun createGuestSession(): Completable
    fun rateFilm(
        film: Film,
        rate: Float,
        guestSessionId: String): Completable

    fun getNowPlayingFilms(): Flowable<List<Film>>
    fun getUpcomingFilms(): Flowable<List<Film>>
    fun getTopRatedFilms(): Flowable<List<Film>>
    fun getPopularFilms(): Flowable<List<Film>>
    fun getUserRatedFilms(): Flowable<List<Film>>
    fun getLikedFilms():Flowable<List<Film>>
    fun getLikedImdbIds(): Flowable<List<Int>>

    fun getRatedFilmById(movieId: Int): Flowable<List<Film>>

    @ExperimentalCoroutinesApi
    fun getUserGuestSessionId(): Single<String>

    fun toggleLike(film: Film): Completable

    fun getFilmDetailsById(id: Int): Flowable<List<FilmDetails>>
    fun deleteOldFilmDetails(): Completable
}
