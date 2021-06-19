package pan.alexander.filmrevealer.domain

import android.util.Log
import androidx.lifecycle.LiveData
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.data.web.pojo.FilmsPage
import pan.alexander.filmrevealer.domain.entities.Film
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) {
    fun loadNowPlayingFilms(page: Int) {
        remoteRepository.loadNowPlayingFilms(page).enqueue(object : Callback<FilmsPage> {
            override fun onResponse(call: Call<FilmsPage>, response: Response<FilmsPage>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        val films = filmsPage.results
                        if (films.isNotEmpty()) {
                            localRepository.deleteNowPlayingFilms()
                            films.forEach { filmDetails ->
                                val film = Film(filmDetails).also {
                                    it.section = Film.Section.NOW_PLAYING.value
                                }
                                localRepository.addFilm(film)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FilmsPage>, t: Throwable) {
                Log.e(LOG_TAG, "Load Now Playing Films failure.", t)
            }
        })

    }

    fun loadUpcomingFilms(page: Int) {
        remoteRepository.loadUpcomingFilms(page).enqueue(object : Callback<FilmsPage> {
            override fun onResponse(call: Call<FilmsPage>, response: Response<FilmsPage>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        val films = filmsPage.results
                        if (films.isNotEmpty()) {
                            localRepository.deleteUpcomingFilms()
                            films.forEach { filmDetails ->
                                val film = Film(filmDetails).also {
                                    it.section = Film.Section.UPCOMING.value
                                }
                                localRepository.addFilm(film)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FilmsPage>, t: Throwable) {
                Log.e(LOG_TAG, "Load Upcoming Films failure.", t)
            }
        })


    }

    fun loadTopRatedFilms(page: Int) {
        remoteRepository.loadTopRatedFilms(page).enqueue(object : Callback<FilmsPage> {
            override fun onResponse(call: Call<FilmsPage>, response: Response<FilmsPage>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        val films = filmsPage.results
                        if (films.isNotEmpty()) {
                            localRepository.deleteTopRatedFilms()
                            films.forEach { filmDetails ->
                                val film = Film(filmDetails).also {
                                    it.section = Film.Section.TOP_RATED.value
                                }
                                localRepository.addFilm(film)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FilmsPage>, t: Throwable) {
                Log.e(LOG_TAG, "Load Top Rated Films failure.", t)
            }

        })
    }

    fun loadPopularFilms(page: Int) {
        remoteRepository.loadPopularFilms(page).enqueue(object : Callback<FilmsPage> {
            override fun onResponse(call: Call<FilmsPage>, response: Response<FilmsPage>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        val films = filmsPage.results
                        if (films.isNotEmpty()) {
                            localRepository.deletePopularFilms()
                            films.forEach { filmDetails ->
                                val film =
                                    Film(filmDetails).also {
                                        it.section = Film.Section.POPULAR.value
                                    }
                                localRepository.addFilm(film)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FilmsPage>, t: Throwable) {
                Log.e(LOG_TAG, "Load Popular Films failure.", t)
            }

        })
    }

    fun getNowPlayingFilms(): LiveData<List<Film>> {
        return localRepository.getNowPlayingFilms()
    }

    fun getUpcomingFilms(): LiveData<List<Film>> {
        return localRepository.getUpcomingFilms()
    }

    fun getTopRatedFilms(): LiveData<List<Film>> {
        return localRepository.getTopRatedFilms()
    }

    fun getPopularFilms(): LiveData<List<Film>> {
        return localRepository.getPopularFilms()
    }

    fun getLikedFilms(): LiveData<List<Film>> {
        return localRepository.getLikedFilms()
    }

    fun toggleLike(film: Film) {
        film.isLiked = !film.isLiked
        localRepository.updateFilm(film)
    }
}
