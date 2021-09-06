package pan.alexander.filmrevealer.data.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import pan.alexander.filmrevealer.domain.local.LocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): LocalRepository {
    override fun getNowPlayingFilms(): Flowable<List<Film>> {
        return localDataSource.getNowPlayingFilms()
    }

    override fun getUpcomingFilms(): Flowable<List<Film>> {
        return localDataSource.getUpcomingFilms()
    }

    override fun getTopRatedFilms(): Flowable<List<Film>> {
        return localDataSource.getTopRatedFilms()
    }

    override fun getPopularFilms(): Flowable<List<Film>> {
        return localDataSource.getPopularFilms()
    }

    override fun getUserRatedFilms(): Flowable<List<Film>> {
        return localDataSource.getUserRatedFilms()
    }

    override fun getLikedFilms(): Flowable<List<Film>> {
        return localDataSource.getLikedFilms()
    }

    override fun getFilmDetailsById(id: Int): Flowable<List<FilmDetails>> {
        return localDataSource.getFilmDetailsById(id)
    }

    override fun getRatedFilmById(movieId: Int): Flowable<List<Film>> {
        return localDataSource.getRatedFilmById(movieId)
    }

    override fun getLikedImdbIds(): Flowable<List<Int>> {
        return localDataSource.getLikedImdbIds()
    }

    override fun getLikedFilmById(movieId: Int): Single<List<Film>> {
        return localDataSource.getLikedFilmById(movieId)
    }

    override fun addFilm(film: Film): Completable {
        return localDataSource.addFilm(film)
    }

    override fun addFilms(films: List<Film>): Completable {
        return localDataSource.addFilms(films)
    }

    override fun deleteNowPlayingFilms(page: Int): Completable {
        return localDataSource.deleteNowPlayingFilms(page)
    }

    override fun deleteUpcomingFilms(page: Int): Completable {
        return localDataSource.deleteUpcomingFilms(page)
    }

    override fun deleteTopRatedFilms(page: Int): Completable {
        return localDataSource.deleteTopRatedFilms(page)
    }

    override fun deletePopularFilms(page: Int): Completable {
        return localDataSource.deletePopularFilms(page)
    }

    override fun deleteUserRatedFilms(page: Int): Completable {
        return localDataSource.deleteUserRatedFilms(page)
    }

    override fun updateFilm(film: Film): Completable {
        return localDataSource.updateFilm(film)
    }

    override fun deleteFilm(film: Film): Completable {
        return localDataSource.deleteFilm(film)
    }

    override fun addFilmDetails(filmDetails: FilmDetails): Completable {
        return localDataSource.addFilmDetails(filmDetails)
    }

    override fun updateFilmDetails(filmDetails: FilmDetails): Completable {
        return localDataSource.updateFilmDetails(filmDetails)
    }

    override fun deleteFilmsDetailsOlderTimestamp(timestamp: Long): Completable {
        return localDataSource.deleteFilmsDetailsOlderTimestamp(timestamp)
    }
}
