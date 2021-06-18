package pan.alexander.filmrevealer.domain

import androidx.lifecycle.LiveData
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
        val response =
            remoteRepository.loadNowPlayingFilms(page).enqueue(object : Callback<FilmsPage> {
                override fun onResponse(call: Call<FilmsPage>, response: Response<FilmsPage>) {
                    if (response.isSuccessful) {
                        response.body()?.let { filmsPage ->
                            val films = filmsPage.results
                            if (films.isNotEmpty()) {
                                localRepository.deleteNowPlayingFilms()
                                films.forEach { filmDetails ->
                                    val film = Film(filmDetails).also {
                                        it.section = Film.Section.NOW_PLAYING
                                    }
                                    localRepository.addFilm(film)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<FilmsPage>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

    }

    fun loadUpcomingFilms(page: Int) {
        val response =
            remoteRepository.loadUpcomingFilms(page).enqueue(object : Callback<FilmsPage> {
                override fun onResponse(call: Call<FilmsPage>, response: Response<FilmsPage>) {
                    if (response.isSuccessful) {
                        response.body()?.let { filmsPage ->
                            val films = filmsPage.results
                            if (films.isNotEmpty()) {
                                localRepository.deleteUpcomingFilms()
                                films.forEach { filmDetails ->
                                    val film = Film(filmDetails).also {
                                        it.section = Film.Section.UPCOMING
                                    }
                                    localRepository.addFilm(film)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<FilmsPage>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })


    }

    fun loadTopRatedFilms(page: Int) {
        val response =
            remoteRepository.loadTopRatedFilms(page).enqueue(object : Callback<FilmsPage> {
                override fun onResponse(call: Call<FilmsPage>, response: Response<FilmsPage>) {
                    if (response.isSuccessful) {
                        response.body()?.let { filmsPage ->
                            val films = filmsPage.results
                            if (films.isNotEmpty()) {
                                localRepository.deleteTopRatedFilms()
                                films.forEach { filmDetails ->
                                    val film = Film(filmDetails).also {
                                        it.section = Film.Section.TOP_RATED
                                    }
                                    localRepository.addFilm(film)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<FilmsPage>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun loadPopularFilms(page: Int) {
        val response =
            remoteRepository.loadPopularFilms(page).enqueue(object : Callback<FilmsPage> {
                override fun onResponse(call: Call<FilmsPage>, response: Response<FilmsPage>) {
                    if (response.isSuccessful) {
                        response.body()?.let { filmsPage ->
                            val films = filmsPage.results
                            if (films.isNotEmpty()) {
                                localRepository.deletePopularFilms()
                                films.forEach { filmDetails ->
                                    val film =
                                        Film(filmDetails).also { it.section = Film.Section.POPULAR }
                                    localRepository.addFilm(film)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<FilmsPage>, t: Throwable) {
                    TODO("Not yet implemented")
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
