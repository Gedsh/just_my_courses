package pan.alexander.filmrevealer.domain

import androidx.lifecycle.LiveData
import pan.alexander.filmrevealer.domain.entities.Film

interface LocalRepository {
    fun getNowPlayingFilms(): LiveData<List<Film>>
    fun getUpcomingFilms(): LiveData<List<Film>>
    fun getTopRatedFilms(): LiveData<List<Film>>
    fun getPopularFilms(): LiveData<List<Film>>
    fun getLikedFilms(): LiveData<List<Film>>
    fun addFilm(film: Film)
    fun deleteNowPlayingFilms()
    fun deleteUpcomingFilms()
    fun deleteTopRatedFilms()
    fun deletePopularFilms()
    fun updateFilm(film: Film)
}
