package pan.alexander.filmrevealer.domain

import android.util.Log
import androidx.lifecycle.LiveData
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.FILM_DETAILS_EXPIRE_PERIOD_MILLISECONDS
import pan.alexander.filmrevealer.data.web.pojo.FilmPreciseDetailsJson
import pan.alexander.filmrevealer.data.web.pojo.FilmsPageJson
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

private const val DELAY_BEFORE_ALLOWING_NEW_REQUEST_MILLISECONDS = 1000L

@Singleton
class MainInteractor @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) {
    private val handler = App.instance.daggerComponent.getMainHandler()

    var loadingNowPlayingFilms = false
        set(value) {
            if (value) {
                field = value
            } else {
                handler.get().postDelayed(
                    { field = value },
                    DELAY_BEFORE_ALLOWING_NEW_REQUEST_MILLISECONDS
                )
            }
        }
    var loadingUpcomingFilms = false
        set(value) {
            if (value) {
                field = value
            } else {
                handler.get().postDelayed(
                    { field = value },
                    DELAY_BEFORE_ALLOWING_NEW_REQUEST_MILLISECONDS
                )
            }
        }
    var loadingTopRatedFilms = false
        set(value) {
            if (value) {
                field = value
            } else {
                handler.get().postDelayed(
                    { field = value },
                    DELAY_BEFORE_ALLOWING_NEW_REQUEST_MILLISECONDS
                )
            }
        }
    var loadingPopularFilms = false
        set(value) {
            if (value) {
                field = value
            } else {
                handler.get().postDelayed(
                    { field = value },
                    DELAY_BEFORE_ALLOWING_NEW_REQUEST_MILLISECONDS
                )
            }
        }

    fun loadNowPlayingFilms(page: Int) {

        if (loadingNowPlayingFilms) {
            return
        }

        loadingNowPlayingFilms = true

        remoteRepository.loadNowPlayingFilms(page).enqueue(object : Callback<FilmsPageJson> {
            override fun onResponse(call: Call<FilmsPageJson>, response: Response<FilmsPageJson>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        val results = filmsPage.results
                        if (results.isNotEmpty()) {
                            val films = mutableListOf<Film>()
                            results.forEach { filmDetails ->
                                val film = Film(filmDetails).also {
                                    it.section = Film.Section.NOW_PLAYING.value
                                    it.page = filmsPage.page
                                    it.totalPages = filmsPage.totalPages
                                }

                                films.add(film)
                            }
                            localRepository.deleteNowPlayingFilms(filmsPage.page)
                            localRepository.addFilms(films)
                        }
                    }
                }
                loadingNowPlayingFilms = false
            }

            override fun onFailure(call: Call<FilmsPageJson>, t: Throwable) {
                loadingNowPlayingFilms = false
                Log.e(LOG_TAG, "Load Now Playing Films failure.", t)
            }
        })

    }

    fun loadUpcomingFilms(page: Int) {

        if (loadingUpcomingFilms) {
            return
        }

        loadingUpcomingFilms = true

        remoteRepository.loadUpcomingFilms(page).enqueue(object : Callback<FilmsPageJson> {
            override fun onResponse(call: Call<FilmsPageJson>, response: Response<FilmsPageJson>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        val results = filmsPage.results
                        if (results.isNotEmpty()) {
                            val films = mutableListOf<Film>()
                            results.forEach { filmDetails ->
                                val film = Film(filmDetails).also {
                                    it.section = Film.Section.UPCOMING.value
                                    it.page = filmsPage.page
                                    it.totalPages = filmsPage.totalPages
                                }
                                films.add(film)
                            }
                            localRepository.deleteUpcomingFilms(filmsPage.page)
                            localRepository.addFilms(films)
                        }
                    }
                }
                loadingUpcomingFilms = false
            }

            override fun onFailure(call: Call<FilmsPageJson>, t: Throwable) {
                loadingUpcomingFilms = false
                Log.e(LOG_TAG, "Load Upcoming Films failure.", t)
            }
        })


    }

    fun loadTopRatedFilms(page: Int) {

        if (loadingTopRatedFilms) {
            return
        }

        loadingTopRatedFilms = true

        remoteRepository.loadTopRatedFilms(page).enqueue(object : Callback<FilmsPageJson> {
            override fun onResponse(call: Call<FilmsPageJson>, response: Response<FilmsPageJson>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        val results = filmsPage.results
                        if (results.isNotEmpty()) {
                            val films = mutableListOf<Film>()
                            results.forEach { filmDetails ->
                                val film = Film(filmDetails).also {
                                    it.section = Film.Section.TOP_RATED.value
                                    it.page = filmsPage.page
                                    it.totalPages = filmsPage.totalPages
                                }
                                films.add(film)
                            }
                            localRepository.deleteTopRatedFilms(filmsPage.page)
                            localRepository.addFilms(films)
                        }
                    }
                }
                loadingTopRatedFilms = false
            }

            override fun onFailure(call: Call<FilmsPageJson>, t: Throwable) {
                loadingTopRatedFilms = false
                Log.e(LOG_TAG, "Load Top Rated Films failure.", t)
            }

        })
    }

    fun loadPopularFilms(page: Int) {

        if (loadingPopularFilms) {
            return
        }

        loadingPopularFilms = true

        remoteRepository.loadPopularFilms(page).enqueue(object : Callback<FilmsPageJson> {
            override fun onResponse(call: Call<FilmsPageJson>, response: Response<FilmsPageJson>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        val results = filmsPage.results
                        if (results.isNotEmpty()) {
                            val films = mutableListOf<Film>()
                            results.forEach { filmDetails ->
                                val film =
                                    Film(filmDetails).also {
                                        it.section = Film.Section.POPULAR.value
                                        it.page = filmsPage.page
                                        it.totalPages = filmsPage.totalPages
                                    }
                                films.add(film)
                            }
                            localRepository.deletePopularFilms(filmsPage.page)
                            localRepository.addFilms(films)
                        }
                    }
                }
                loadingPopularFilms = false
            }

            override fun onFailure(call: Call<FilmsPageJson>, t: Throwable) {
                loadingPopularFilms = false
                Log.e(LOG_TAG, "Load Popular Films failure.", t)
            }

        })
    }

    fun loadFilmPreciseDetails(movieId: Int) {
        remoteRepository.loadFilmPreciseDetails(movieId)
            .enqueue(object : Callback<FilmPreciseDetailsJson> {
                override fun onResponse(
                    call: Call<FilmPreciseDetailsJson>,
                    response: Response<FilmPreciseDetailsJson>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { details ->
                            if (localRepository.getFilmDetailsById(movieId).value.isNullOrEmpty()) {
                                localRepository.addFilmDetails(FilmDetails(details))
                            } else {
                                localRepository.updateFilmDetails(FilmDetails(details))
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<FilmPreciseDetailsJson>, t: Throwable) {
                    Log.e(LOG_TAG, "Load Film precise details failure.", t)
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

    fun getFilmDetailsById(id: Int): LiveData<List<FilmDetails>> {
        return localRepository.getFilmDetailsById(id)
    }

    fun deleteOldFilmDetails() {
        localRepository.deleteFilmsDetailsOlderTimestamp(
            System.currentTimeMillis() - FILM_DETAILS_EXPIRE_PERIOD_MILLISECONDS
        )
    }
}
