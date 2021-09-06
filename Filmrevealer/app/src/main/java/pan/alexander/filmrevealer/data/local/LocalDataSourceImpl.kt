package pan.alexander.filmrevealer.data.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import pan.alexander.filmrevealer.database.FilmDetailsDao
import pan.alexander.filmrevealer.database.FilmDao
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val filmDao: FilmDao,
    private val filmDetailsDao: FilmDetailsDao
) : LocalDataSource {
    override fun getNowPlayingFilms(): Flowable<List<Film>> {
        return filmDao.getFilmsForSection(Film.Section.NOW_PLAYING.value)
    }

    override fun getUpcomingFilms(): Flowable<List<Film>> {
        return filmDao.getFilmsForSection(Film.Section.UPCOMING.value)
    }

    override fun getTopRatedFilms(): Flowable<List<Film>> {
        return filmDao.getFilmsForSection(Film.Section.TOP_RATED.value)
    }

    override fun getPopularFilms(): Flowable<List<Film>> {
        return filmDao.getFilmsForSection(Film.Section.POPULAR.value)
    }

    override fun getUserRatedFilms(): Flowable<List<Film>> {
        return filmDao.getFilmsForSectionOrderByAdding(Film.Section.USER_RATED.value)
    }

    override fun getLikedFilms(): Flowable<List<Film>> {
        return filmDao.getFilmsForSectionOrderByAdding(Film.Section.LIKED.value)
    }

    override fun getLikedImdbIds(): Flowable<List<Int>> {
        return filmDao.getLikedImdbIds(Film.Section.LIKED.value)
    }

    override fun getRatedFilmById(movieId: Int): Flowable<List<Film>> {
        return filmDao.getRatedFilmById(movieId, Film.Section.USER_RATED.value)
    }

    override fun getFilmDetailsById(id: Int): Flowable<List<FilmDetails>> {
        return filmDetailsDao.getFilmDetailsById(id)
    }

    override fun addFilm(film: Film): Completable {
        return filmDao.insert(film)
    }

    override fun addFilms(films: List<Film>): Completable {
        return filmDao.insertFilms(films)
    }

    override fun deleteNowPlayingFilms(page: Int): Completable {
        return filmDao.deleteAllFilmsFromSection(Film.Section.NOW_PLAYING.value, page)
    }

    override fun deleteUpcomingFilms(page: Int): Completable {
        return filmDao.deleteAllFilmsFromSection(Film.Section.UPCOMING.value, page)
    }

    override fun deleteTopRatedFilms(page: Int): Completable {
        return filmDao.deleteAllFilmsFromSection(Film.Section.TOP_RATED.value, page)
    }

    override fun deletePopularFilms(page: Int): Completable {
        return filmDao.deleteAllFilmsFromSection(Film.Section.POPULAR.value, page)
    }

    override fun deleteUserRatedFilms(page: Int): Completable {
        return filmDao.deleteAllFilmsFromSection(Film.Section.USER_RATED.value, page)
    }

    override fun updateFilm(film: Film): Completable {
        return filmDao.update(film)
    }

    override fun deleteFilm(film: Film): Completable {
        return filmDao.delete(film)
    }

    override fun addFilmDetails(filmDetails: FilmDetails): Completable {
        return filmDetailsDao.insertFilmDetails(filmDetails)
    }

    override fun updateFilmDetails(filmDetails: FilmDetails): Completable {
        return filmDetailsDao.updateFilmDetails(filmDetails)
    }

    override fun deleteFilmsDetailsOlderTimestamp(timestamp: Long): Completable {
        return filmDetailsDao.deleteFilmsDetailsOlderTimestamp(timestamp)
    }

    override fun getLikedFilmById(movieId: Int): Single<List<Film>> {
        return filmDao.getLikedFilmById(movieId, Film.Section.LIKED.value)
    }
}
