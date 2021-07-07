package pan.alexander.filmrevealer.domain

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.FILM_DETAILS_EXPIRE_PERIOD_MILLISECONDS
import pan.alexander.filmrevealer.R
import pan.alexander.filmrevealer.data.web.pojo.FilmPreciseDetailsJson
import pan.alexander.filmrevealer.data.web.pojo.FilmsPageJson
import pan.alexander.filmrevealer.data.web.pojo.ServerResponse
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

private const val DELAY_BEFORE_ALLOWING_NEW_REQUEST_MILLISECONDS = 3000L

@Singleton
class MainInteractor @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val preferencesRepository: PreferencesRepository
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

    var loadingFilmsRatedByUser = false
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

    fun loadNowPlayingFilms(page: Int, block: (error: String) -> Unit) {

        if (loadingNowPlayingFilms) {
            return
        }

        loadingNowPlayingFilms = true

        remoteRepository.loadNowPlayingFilms(page).enqueue(object : Callback<FilmsPageJson> {
            override fun onResponse(call: Call<FilmsPageJson>, response: Response<FilmsPageJson>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        updateNowPlayingFilms(filmsPage)
                    }
                }
                loadingNowPlayingFilms = false
            }

            override fun onFailure(call: Call<FilmsPageJson>, t: Throwable) {
                loadingNowPlayingFilms = false

                t.message?.let { message ->
                    block(
                        App.instance.getString(R.string.load_now_playing_films_failure)
                                + ": " + message
                    )
                }

                Log.e(LOG_TAG, "Load Now Playing Films failure.", t)
            }
        })

    }

    private fun updateNowPlayingFilms(filmsPage: FilmsPageJson) {
        val results = filmsPage.results
        if (results.isNotEmpty()) {
            val films = mutableListOf<Film>()
            results.forEach { filmDetails ->
                val film = Film(filmDetails).apply {
                    section = Film.Section.NOW_PLAYING.value
                    page = filmsPage.page
                    totalPages = filmsPage.totalPages
                }

                films.add(film)
            }

            with(localRepository) {
                deleteNowPlayingFilms(filmsPage.page)
                addFilms(films)
            }
        }
    }

    fun loadUpcomingFilms(page: Int, block: (error: String) -> Unit) {

        if (loadingUpcomingFilms) {
            return
        }

        loadingUpcomingFilms = true

        remoteRepository.loadUpcomingFilms(page).enqueue(object : Callback<FilmsPageJson> {
            override fun onResponse(call: Call<FilmsPageJson>, response: Response<FilmsPageJson>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        updateUpcomingFilms(filmsPage)
                    }
                }
                loadingUpcomingFilms = false
            }

            override fun onFailure(call: Call<FilmsPageJson>, t: Throwable) {
                loadingUpcomingFilms = false
                t.message?.let { message ->
                    block(
                        App.instance.getString(R.string.load_upcoming_films_failure)
                                + ": " + message
                    )
                }
                Log.e(LOG_TAG, "Load Upcoming Films failure.", t)
            }
        })


    }

    private fun updateUpcomingFilms(filmsPage: FilmsPageJson) {
        val results = filmsPage.results
        if (results.isNotEmpty()) {
            val films = mutableListOf<Film>()
            results.forEach { filmDetails ->
                val film = Film(filmDetails).apply {
                    section = Film.Section.UPCOMING.value
                    page = filmsPage.page
                    totalPages = filmsPage.totalPages
                }
                films.add(film)
            }

            with(localRepository) {
                deleteUpcomingFilms(filmsPage.page)
                addFilms(films)
            }
        }
    }

    fun loadTopRatedFilms(page: Int, block: (error: String) -> Unit) {

        if (loadingTopRatedFilms) {
            return
        }

        loadingTopRatedFilms = true

        remoteRepository.loadTopRatedFilms(page).enqueue(object : Callback<FilmsPageJson> {
            override fun onResponse(call: Call<FilmsPageJson>, response: Response<FilmsPageJson>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        updateToRatedFilms(filmsPage)
                    }
                }
                loadingTopRatedFilms = false
            }

            override fun onFailure(call: Call<FilmsPageJson>, t: Throwable) {
                loadingTopRatedFilms = false
                t.message?.let { message ->
                    block(
                        App.instance.getString(R.string.load_top_rated_films_failure)
                                + ": " + message
                    )
                }
                Log.e(LOG_TAG, "Load Top Rated Films failure.", t)
            }

        })
    }

    private fun updateToRatedFilms(filmsPage: FilmsPageJson) {
        val results = filmsPage.results
        if (results.isNotEmpty()) {
            val films = mutableListOf<Film>()
            results.forEach { filmDetails ->
                val film = Film(filmDetails).apply {
                    section = Film.Section.TOP_RATED.value
                    page = filmsPage.page
                    totalPages = filmsPage.totalPages
                }
                films.add(film)
            }

            with(localRepository) {
                deleteTopRatedFilms(filmsPage.page)
                addFilms(films)
            }
        }
    }

    fun loadPopularFilms(page: Int, block: (error: String) -> Unit) {

        if (loadingPopularFilms) {
            return
        }

        loadingPopularFilms = true

        remoteRepository.loadPopularFilms(page).enqueue(object : Callback<FilmsPageJson> {
            override fun onResponse(call: Call<FilmsPageJson>, response: Response<FilmsPageJson>) {
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        updatePopularFilms(filmsPage)
                    }
                }
                loadingPopularFilms = false
            }

            override fun onFailure(call: Call<FilmsPageJson>, t: Throwable) {
                loadingPopularFilms = false
                t.message?.let { message ->
                    block(
                        App.instance.getString(R.string.load_popular_films_failure)
                                + ": " + message
                    )
                }
                Log.e(LOG_TAG, "Load Popular Films failure.", t)
            }

        })
    }

    private fun updatePopularFilms(filmsPage: FilmsPageJson) {
        val results = filmsPage.results
        if (results.isNotEmpty()) {
            val films = mutableListOf<Film>()
            results.forEach { filmDetails ->
                val film =
                    Film(filmDetails).apply {
                        section = Film.Section.POPULAR.value
                        page = filmsPage.page
                        totalPages = filmsPage.totalPages
                    }
                films.add(film)
            }

            with(localRepository) {
                deletePopularFilms(filmsPage.page)
                addFilms(films)
            }
        }
    }

    fun loadFilmPreciseDetails(movieId: Int, block: (error: String) -> Unit) {
        remoteRepository.loadFilmPreciseDetails(movieId)
            .enqueue(object : Callback<FilmPreciseDetailsJson> {
                override fun onResponse(
                    call: Call<FilmPreciseDetailsJson>,
                    response: Response<FilmPreciseDetailsJson>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { details ->
                            updateFilmPreciseDetails(movieId, details)
                        }
                    }
                }

                override fun onFailure(call: Call<FilmPreciseDetailsJson>, t: Throwable) {
                    t.message?.let { message ->
                        block(
                            App.instance.getString(R.string.load_film_details_failure)
                                    + ": " + message
                        )
                    }
                    Log.e(LOG_TAG, "Load Film precise details failure.", t)
                }

            })
    }

    private fun updateFilmPreciseDetails(movieId: Int, details: FilmPreciseDetailsJson) {
        if (localRepository.getFilmDetailsById(movieId).value.isNullOrEmpty()) {
            localRepository.addFilmDetails(FilmDetails(details))
        } else {
            localRepository.updateFilmDetails(FilmDetails(details))
        }
    }

    suspend fun createGuestSession(block: (error: String) -> Unit) = withContext(Dispatchers.IO) {
        try {
            remoteRepository.createGuestSession().execute().let { response ->
                if (response.isSuccessful) {
                    response.body()?.let { session ->
                        preferencesRepository.setGuestSessionId(session.guestSessionId)
                    }
                }
            }
        } catch (t: Throwable) {
            t.message?.let { message ->
                block(
                    App.instance.getString(R.string.create_guest_session_failure)
                            + ": " + message
                )
            }
            Log.e(LOG_TAG, "Failed to create guest session.", t)
        }

    }

    fun loadUserRatedFilms(guestSessionId: String, block: (error: String) -> Unit) {

        if (loadingFilmsRatedByUser) {
            return
        }

        loadingFilmsRatedByUser = true

        remoteRepository.getUserRatedFilms(guestSessionId)
            .enqueue(object : Callback<FilmsPageJson> {
                override fun onResponse(
                    call: Call<FilmsPageJson>,
                    response: Response<FilmsPageJson>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { filmsPage ->
                            updateUserRatedFilms(filmsPage)
                        }
                    }
                    loadingFilmsRatedByUser = false
                }

                override fun onFailure(call: Call<FilmsPageJson>, t: Throwable) {
                    loadingFilmsRatedByUser = false
                    t.message?.let { message ->
                        block(
                            App.instance.getString(R.string.load_film_rated_by_user_failure)
                                    + ": " + message
                        )
                    }
                    Log.e(LOG_TAG, "Load films rated by user failure.", t)
                }

            })
    }

    private fun updateUserRatedFilms(filmsPage: FilmsPageJson) {
        val results = filmsPage.results
        if (results.isNotEmpty()) {
            val films = mutableListOf<Film>()
            results.forEach { filmDetails ->
                val film = Film(filmDetails).apply {
                    section = Film.Section.USER_RATED.value
                    page = filmsPage.page
                    totalPages = filmsPage.totalPages
                }
                films.add(film)
            }

            with(localRepository) {
                deleteUserRatedFilms(filmsPage.page)
                addFilms(films)
            }
        }
    }

    fun rateFilm(
        film: Film,
        rate: Float,
        guestSessionId: String,
        block: (error: String) -> Unit
    ) {
        remoteRepository.rateFilm(film.movieId, rate, guestSessionId)
            .enqueue(object : Callback<ServerResponse> {
                override fun onResponse(
                    call: Call<ServerResponse>,
                    response: Response<ServerResponse>
                ) {
                    if (response.isSuccessful) {
                        localRepository.addFilm(film)
                    }
                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    t.message?.let { message ->
                        block(
                            App.instance.getString(R.string.rate_film_failure)
                                    + ": " + message
                        )
                    }
                    Log.e(LOG_TAG, "Failed to rate the film.", t)
                }

            })
    }

    fun getNowPlayingFilms() = localRepository.getNowPlayingFilms()

    fun getUpcomingFilms() = localRepository.getUpcomingFilms()

    fun getTopRatedFilms() = localRepository.getTopRatedFilms()

    fun getPopularFilms() = localRepository.getPopularFilms()

    fun getUserGuestSessionId() = preferencesRepository.getGuestSessionId()

    fun getUserRatedFilms() = localRepository.getUserRatedFilms()

    fun getRatedFilmById(movieId: Int) = localRepository.getRatedFilmById(movieId)

    fun getLikedFilms() = localRepository.getLikedFilms()

    fun toggleLike(film: Film) =
        localRepository.apply { updateFilm(film.also { it.isLiked = !it.isLiked }) }

    fun getFilmDetailsById(id: Int) = localRepository.getFilmDetailsById(id)

    fun deleteOldFilmDetails() =
        localRepository.deleteFilmsDetailsOlderTimestamp(
            System.currentTimeMillis() - FILM_DETAILS_EXPIRE_PERIOD_MILLISECONDS
        )

}
