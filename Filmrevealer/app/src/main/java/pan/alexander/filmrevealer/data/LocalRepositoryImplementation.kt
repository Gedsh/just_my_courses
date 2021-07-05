package pan.alexander.filmrevealer.data

import androidx.lifecycle.LiveData
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.domain.LocalRepository
import pan.alexander.filmrevealer.domain.entities.Film

class LocalRepositoryImplementation : LocalRepository {
    private val dao = App.instance.daggerComponent.getFilmDao()

    override fun getNowPlayingFilms(): LiveData<List<Film>> {
        return dao.get().getFilmsForSection(Film.Section.NOW_PLAYING.value)
    }

    override fun getUpcomingFilms(): LiveData<List<Film>> {
        return dao.get().getFilmsForSection(Film.Section.UPCOMING.value)
    }

    override fun getTopRatedFilms(): LiveData<List<Film>> {
        return dao.get().getFilmsForSection(Film.Section.TOP_RATED.value)
    }

    override fun getPopularFilms(): LiveData<List<Film>> {
        return dao.get().getFilmsForSection(Film.Section.POPULAR.value)
    }

    override fun getLikedFilms(): LiveData<List<Film>> {
        return dao.get().getLikedFilms()
    }

    override fun addFilm(film: Film) {
        dao.get().insert(film)
    }

    override fun deleteNowPlayingFilms() {
        dao.get().deleteAllFilmsFromSection(Film.Section.NOW_PLAYING.value)
    }

    override fun deleteUpcomingFilms() {
        dao.get().deleteAllFilmsFromSection(Film.Section.UPCOMING.value)
    }

    override fun deleteTopRatedFilms() {
        dao.get().deleteAllFilmsFromSection(Film.Section.TOP_RATED.value)
    }

    override fun deletePopularFilms() {
        dao.get().deleteAllFilmsFromSection(Film.Section.POPULAR.value)
    }

    override fun updateFilm(film: Film) {
        dao.get().update(film)
    }
}
