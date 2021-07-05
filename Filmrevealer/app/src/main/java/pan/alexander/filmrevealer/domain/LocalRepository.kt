package pan.alexander.filmrevealer.domain

import androidx.lifecycle.LiveData
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails

interface LocalRepository {
    fun getNowPlayingFilms(): LiveData<List<Film>>
    fun getUpcomingFilms(): LiveData<List<Film>>
    fun getTopRatedFilms(): LiveData<List<Film>>
    fun getPopularFilms(): LiveData<List<Film>>
    fun getUserRatedFilms(): LiveData<List<Film>>
    fun getLikedFilms(): LiveData<List<Film>>
    fun addFilm(film: Film)
    fun addFilms(films: List<Film>)
    fun deleteNowPlayingFilms(page: Int)
    fun deleteUpcomingFilms(page: Int)
    fun deleteTopRatedFilms(page: Int)
    fun deletePopularFilms(page: Int)
    fun deleteUserRatedFilms(page: Int)
    fun updateFilm(film: Film)
    fun deleteFilm(film: Film)

    fun getFilmDetailsById(id: Int): LiveData<List<FilmDetails>>
    fun addFilmDetails(filmDetails: FilmDetails)
    fun updateFilmDetails(filmDetails: FilmDetails)
    fun deleteFilmsDetailsOlderTimestamp(timestamp: Long)

    fun getRatedFilmById(movieId: Int): LiveData<List<Film>>
    fun getLikedFilmById(movieId: Int): Film?
    fun getLikedImdbIds(): LiveData<List<Int>>
}
