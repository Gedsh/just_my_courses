package pan.alexander.filmrevealer.domain.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails

interface LocalRepository {
    fun getNowPlayingFilms(): Flowable<List<Film>>
    fun getUpcomingFilms(): Flowable<List<Film>>
    fun getTopRatedFilms(): Flowable<List<Film>>
    fun getPopularFilms(): Flowable<List<Film>>
    fun getUserRatedFilms(): Flowable<List<Film>>
    fun getLikedFilms(): Flowable<List<Film>>
    fun getFilmDetailsById(id: Int): Flowable<List<FilmDetails>>
    fun getRatedFilmById(movieId: Int): Flowable<List<Film>>
    fun getLikedImdbIds(): Flowable<List<Int>>
    fun getLikedFilmById(movieId: Int): Single<List<Film>>

    fun addFilm(film: Film): Completable
    fun addFilms(films: List<Film>): Completable
    fun deleteNowPlayingFilms(page: Int): Completable
    fun deleteUpcomingFilms(page: Int): Completable
    fun deleteTopRatedFilms(page: Int): Completable
    fun deletePopularFilms(page: Int): Completable
    fun deleteUserRatedFilms(page: Int): Completable
    fun updateFilm(film: Film): Completable
    fun deleteFilm(film: Film): Completable
    fun addFilmDetails(filmDetails: FilmDetails): Completable
    fun updateFilmDetails(filmDetails: FilmDetails): Completable
    fun deleteFilmsDetailsOlderTimestamp(timestamp: Long): Completable
}
