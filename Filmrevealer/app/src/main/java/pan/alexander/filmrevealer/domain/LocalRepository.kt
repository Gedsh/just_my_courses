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
    suspend fun addFilm(film: Film)
    suspend fun addFilms(films: List<Film>)
    suspend fun deleteNowPlayingFilms(page: Int)
    suspend fun deleteUpcomingFilms(page: Int)
    suspend fun deleteTopRatedFilms(page: Int)
    suspend fun deletePopularFilms(page: Int)
    suspend fun deleteUserRatedFilms(page: Int)
    suspend fun updateFilm(film: Film)
    suspend fun deleteFilm(film: Film)

    fun getFilmDetailsById(id: Int): LiveData<List<FilmDetails>>
    suspend fun addFilmDetails(filmDetails: FilmDetails)
    suspend fun updateFilmDetails(filmDetails: FilmDetails)
    suspend fun deleteFilmsDetailsOlderTimestamp(timestamp: Long)

    fun getRatedFilmById(movieId: Int): LiveData<List<Film>>
    suspend fun getLikedFilmById(movieId: Int): Film?
    fun getLikedImdbIds(): LiveData<List<Int>>
}
