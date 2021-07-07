package pan.alexander.filmrevealer.domain

import android.util.Log
import androidx.annotation.UiThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.utils.FILM_DETAILS_EXPIRE_PERIOD_MILLISECONDS
import pan.alexander.filmrevealer.R
import pan.alexander.filmrevealer.data.web.pojo.FilmPreciseDetailsJson
import pan.alexander.filmrevealer.data.web.pojo.FilmsPageJson
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import javax.inject.Inject
import javax.inject.Singleton

private const val DELAY_BEFORE_ALLOWING_NEW_REQUEST_MILLISECONDS = 3000L

@Singleton
class MainInteractor @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val preferencesRepository: PreferencesRepository,
    private val dispatcherIO: CoroutineDispatcher
) {
    private val handler = App.instance.daggerComponent.getMainHandler()

    private var loadingNowPlayingFilms = false
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
    private var loadingUpcomingFilms = false
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
    private var loadingTopRatedFilms = false
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
    private var loadingPopularFilms = false
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

    private var loadingFilmsRatedByUser = false
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

    @UiThread
    suspend fun loadNowPlayingFilms(page: Int, block: (error: String) -> Unit) {

        if (loadingNowPlayingFilms) {
            return
        }

        loadingNowPlayingFilms = true

        try {
            kotlin.runCatching {
                remoteRepository.loadNowPlayingFilms(page)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        updateNowPlayingFilms(filmsPage)
                    }
                } else {
                    response.errorBody()?.let { message ->
                        block(
                            App.instance.getString(R.string.load_now_playing_films_failure)
                                    + ": " + message
                        )
                    }
                }

            }.onFailure {
                it.message?.let { message ->
                    block(
                        App.instance.getString(R.string.load_now_playing_films_failure)
                                + ": " + message
                    )
                }

                Log.e(LOG_TAG, "Load Now Playing Films failure.", it)
            }

        } finally {
            loadingNowPlayingFilms = false
        }

    }

    private suspend fun updateNowPlayingFilms(filmsPage: FilmsPageJson) {
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

    @UiThread
    suspend fun loadUpcomingFilms(page: Int, block: (error: String) -> Unit) {

        if (loadingUpcomingFilms) {
            return
        }

        loadingUpcomingFilms = true

        try {
            kotlin.runCatching {
                remoteRepository.loadUpcomingFilms(page)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        updateUpcomingFilms(filmsPage)
                    }
                } else {
                    response.errorBody()?.let { message ->
                        block(
                            App.instance.getString(R.string.load_upcoming_films_failure)
                                    + ": " + message
                        )
                    }
                }

            }.onFailure {
                it.message?.let { message ->
                    block(
                        App.instance.getString(R.string.load_upcoming_films_failure)
                                + ": " + message
                    )
                }
                Log.e(LOG_TAG, "Load Upcoming Films failure.", it)
            }
        } finally {
            loadingUpcomingFilms = false
        }

    }

    private suspend fun updateUpcomingFilms(filmsPage: FilmsPageJson) {
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

    @UiThread
    suspend fun loadTopRatedFilms(page: Int, block: (error: String) -> Unit) {

        if (loadingTopRatedFilms) {
            return
        }

        loadingTopRatedFilms = true

        try {
            kotlin.runCatching {
                remoteRepository.loadTopRatedFilms(page)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        updateToRatedFilms(filmsPage)
                    }
                } else {
                    response.errorBody()?.let { message ->
                        block(
                            App.instance.getString(R.string.load_top_rated_films_failure)
                                    + ": " + message
                        )
                    }
                }

            }.onFailure {
                it.message?.let { message ->
                    block(
                        App.instance.getString(R.string.load_top_rated_films_failure)
                                + ": " + message
                    )
                }
                Log.e(LOG_TAG, "Load Top Rated Films failure.", it)
            }
        } finally {
            loadingTopRatedFilms = false
        }

    }

    private suspend fun updateToRatedFilms(filmsPage: FilmsPageJson) {
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

    @UiThread
    suspend fun loadPopularFilms(page: Int, block: (error: String) -> Unit) {

        if (loadingPopularFilms) {
            return
        }

        loadingPopularFilms = true

        try {
            kotlin.runCatching {
                remoteRepository.loadPopularFilms(page)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        updatePopularFilms(filmsPage)
                    }
                } else {
                    response.errorBody()?.let { message ->
                        block(
                            App.instance.getString(R.string.load_popular_films_failure)
                                    + ": " + message
                        )
                    }
                }
            }.onFailure {
                it.message?.let { message ->
                    block(
                        App.instance.getString(R.string.load_popular_films_failure)
                                + ": " + message
                    )
                }
                Log.e(LOG_TAG, "Load Popular Films failure.", it)
            }
        } finally {
            loadingPopularFilms = false
        }

    }

    private suspend fun updatePopularFilms(filmsPage: FilmsPageJson) {
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

    suspend fun loadFilmPreciseDetails(movieId: Int, block: suspend (error: String) -> Unit) {

        kotlin.runCatching {
            remoteRepository.loadFilmPreciseDetails(movieId)
        }.onSuccess { response ->
            if (response.isSuccessful) {
                response.body()?.let { details ->
                    updateFilmPreciseDetails(movieId, details)
                }
            } else {
                response.errorBody()?.let { message ->
                    block(
                        App.instance.getString(R.string.load_film_details_failure)
                                + ": " + message
                    )
                }
            }
        }.onFailure {
            it.message?.let { message ->
                block(
                    App.instance.getString(R.string.load_film_details_failure)
                            + ": " + message
                )
            }
            Log.e(LOG_TAG, "Load Film precise details failure.", it)
        }
    }

    private suspend fun updateFilmPreciseDetails(movieId: Int, details: FilmPreciseDetailsJson) {
        if (localRepository.getFilmDetailsById(movieId).value.isNullOrEmpty()) {
            localRepository.addFilmDetails(FilmDetails(details))
        } else {
            localRepository.updateFilmDetails(FilmDetails(details))
        }
    }

    suspend fun createGuestSession(block: (error: String) -> Unit) = withContext(dispatcherIO) {
        kotlin.runCatching {
            remoteRepository.createGuestSession()
        }.onSuccess { response ->
            if (response.isSuccessful) {
                response.body()?.let { session ->
                    preferencesRepository.setGuestSessionId(session.guestSessionId)
                }
            }
        }.onFailure { t ->
            t.message?.let { message ->
                block(
                    App.instance.getString(R.string.create_guest_session_failure)
                            + ": " + message
                )
            }
            Log.e(LOG_TAG, "Failed to create guest session.", t)
        }
    }

    @UiThread
    suspend fun loadUserRatedFilms(guestSessionId: String, block: (error: String) -> Unit) {

        if (loadingFilmsRatedByUser) {
            return
        }

        loadingFilmsRatedByUser = true

        try {
            kotlin.runCatching {
                remoteRepository.getUserRatedFilms(guestSessionId)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    response.body()?.let { filmsPage ->
                        updateUserRatedFilms(filmsPage)
                    }
                } else {
                    response.errorBody()?.let { message ->
                        block(
                            App.instance.getString(R.string.load_film_rated_by_user_failure)
                                    + ": " + message
                        )
                    }
                }

            }.onFailure {
                it.message?.let { message ->
                    block(
                        App.instance.getString(R.string.load_film_rated_by_user_failure)
                                + ": " + message
                    )
                }
                Log.e(LOG_TAG, "Load films rated by user failure.", it)
            }
        } finally {
            loadingFilmsRatedByUser = false
        }

    }

    private suspend fun updateUserRatedFilms(filmsPage: FilmsPageJson) {
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

    suspend fun rateFilm(
        film: Film,
        rate: Float,
        guestSessionId: String,
        block: (error: String) -> Unit
    ) {

        kotlin.runCatching {
            remoteRepository.rateFilm(film.movieId, rate, guestSessionId)
        }.onSuccess { response ->
            if (response.isSuccessful) {
                localRepository.addFilm(film)
            } else {
                response.errorBody()?.let { message ->
                    block(
                        App.instance.getString(R.string.rate_film_failure)
                                + ": " + message
                    )
                }
            }
        }.onFailure {
            it.message?.let { message ->
                block(
                    App.instance.getString(R.string.rate_film_failure)
                            + ": " + message
                )
            }
            Log.e(LOG_TAG, "Failed to rate the film.", it)
        }
    }

    fun getNowPlayingFilms() = localRepository.getNowPlayingFilms()

    fun getUpcomingFilms() = localRepository.getUpcomingFilms()

    fun getTopRatedFilms() = localRepository.getTopRatedFilms()

    fun getPopularFilms() = localRepository.getPopularFilms()

    suspend fun getUserGuestSessionId() = withContext(dispatcherIO) {
        preferencesRepository.getGuestSessionId()
    }

    fun getUserRatedFilms() = localRepository.getUserRatedFilms()

    fun getRatedFilmById(movieId: Int) = localRepository.getRatedFilmById(movieId)

    fun getLikedFilms() = localRepository.getLikedFilms()

    fun getLikedImdbIds() = localRepository.getLikedImdbIds()

    suspend fun toggleLike(film: Film) {
        val likedFilm = localRepository.getLikedFilmById(film.movieId)
        if (likedFilm == null) {
            localRepository.addFilm(
                film.apply {
                    id = 0
                    page = 1
                    totalPages = 1
                    section = Film.Section.LIKED.value
                }
            )
        } else {
            localRepository.deleteFilm(likedFilm)
        }
    }

    fun getFilmDetailsById(id: Int) = localRepository.getFilmDetailsById(id)

    suspend fun deleteOldFilmDetails() =
        localRepository.deleteFilmsDetailsOlderTimestamp(
            System.currentTimeMillis() - FILM_DETAILS_EXPIRE_PERIOD_MILLISECONDS
        )

}
