package pan.alexander.filmrevealer.data

import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.domain.LocalRepository
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails

class LocalRepositoryImplementation : LocalRepository {
    private val filmDao = App.instance.daggerComponent.getFilmDao()
    private val filmDetailsDao = App.instance.daggerComponent.getFilmDetailsDao()

    override fun getNowPlayingFilms() =
        filmDao.get().getFilmsForSection(Film.Section.NOW_PLAYING.value)

    override fun getUpcomingFilms() = filmDao.get().getFilmsForSection(Film.Section.UPCOMING.value)

    override fun getTopRatedFilms() = filmDao.get().getFilmsForSection(Film.Section.TOP_RATED.value)

    override fun getPopularFilms() = filmDao.get().getFilmsForSection(Film.Section.POPULAR.value)

    override fun getUserRatedFilms() =
        filmDao.get().getFilmsForSection(Film.Section.USER_RATED.value)

    override fun getLikedFilms() = filmDao.get().getLikedFilms()

    override fun addFilm(film: Film) = filmDao.get().insert(film)

    override fun addFilms(films: List<Film>) = filmDao.get().insertFilms(films)

    override fun deleteNowPlayingFilms(page: Int) =
        filmDao.get().deleteAllFilmsFromSection(Film.Section.NOW_PLAYING.value, page)

    override fun deleteUpcomingFilms(page: Int) =
        filmDao.get().deleteAllFilmsFromSection(Film.Section.UPCOMING.value, page)

    override fun deleteTopRatedFilms(page: Int) =
        filmDao.get().deleteAllFilmsFromSection(Film.Section.TOP_RATED.value, page)

    override fun deletePopularFilms(page: Int) =
        filmDao.get().deleteAllFilmsFromSection(Film.Section.POPULAR.value, page)

    override fun deleteUserRatedFilms(page: Int) =
        filmDao.get().deleteAllFilmsFromSection(Film.Section.USER_RATED.value, page)

    override fun updateFilm(film: Film) = filmDao.get().update(film)

    override fun getFilmDetailsById(id: Int) = filmDetailsDao.get().getFilmDetailsById(id)

    override fun addFilmDetails(filmDetails: FilmDetails) =
        filmDetailsDao.get().insertFilmDetails(filmDetails)

    override fun updateFilmDetails(filmDetails: FilmDetails) =
        filmDetailsDao.get().updateFilmDetails(filmDetails)

    override fun deleteFilmsDetailsOlderTimestamp(timestamp: Long) =
        filmDetailsDao.get().deleteFilmsDetailsOlderTimestamp(timestamp)

    override fun getRatedFilmById(movieId: Int) =
        filmDao.get().getRatedFilmById(movieId, Film.Section.USER_RATED.value)
}
