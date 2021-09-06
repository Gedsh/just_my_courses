package pan.alexander.filmrevealer.data.remote

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import pan.alexander.filmrevealer.data.remote.pojo.FilmsPageJson
import pan.alexander.filmrevealer.data.remote.pojo.ServerResponse
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import pan.alexander.filmrevealer.domain.remote.RemoteRepository
import pan.alexander.filmrevealer.utils.FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS
import pan.alexander.filmrevealer.utils.FILMS_UPDATE_RETRIES_COUNT
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val serverResponseToFilmsMapper: ServerResponseToFilmsMapper,
    private val serverResponseToFilmDetailsMapper: ServerResponseToFilmDetailsMapper
) : RemoteRepository {

    override fun getNowPlayingFilms(page: Int): Flowable<List<Film>> {
        return filmsPageToFlowable(
            remoteDataSource.loadNowPlayingFilms(page),
            Film.Section.NOW_PLAYING
        )
    }

    override fun getUpcomingFilms(page: Int): Flowable<List<Film>> {
        return filmsPageToFlowable(
            remoteDataSource.loadUpcomingFilms(page),
            Film.Section.UPCOMING
        )
    }

    override fun getTopRatedFilms(page: Int): Flowable<List<Film>> {
        return filmsPageToFlowable(
            remoteDataSource.loadTopRatedFilms(page),
            Film.Section.TOP_RATED
        )
    }

    override fun getPopularFilms(page: Int): Flowable<List<Film>> {
        return filmsPageToFlowable(
            remoteDataSource.loadPopularFilms(page),
            Film.Section.POPULAR
        )
    }

    override fun getUserRatedFilms(guestSessionId: String): Flowable<List<Film>> {
        return filmsPageToFlowable(
            remoteDataSource.getUserRatedFilms(guestSessionId),
            Film.Section.USER_RATED
        )
    }

    private fun filmsPageToFlowable(
        singleResponse: Single<Response<FilmsPageJson>>, section: Film.Section
    ): Flowable<List<Film>> {
        return singleResponse.map { response ->
            var films: List<Film>? = null
            if (response.isSuccessful) {
                response.body()?.let { filmsPage ->
                    films = serverResponseToFilmsMapper.map(filmsPage, section)
                }
            } else {
                response.errorBody()?.let { message ->
                    throw IOException(message.string())
                }
            }
            films ?: emptyList()
        }
            .retryWhen { throwableFlowable ->
                val counter = AtomicInteger()
                throwableFlowable
                    .flatMap {
                        if (it is IOException
                            && counter.getAndIncrement() != FILMS_UPDATE_RETRIES_COUNT
                        ) {
                            Flowable.timer(counter.get().toLong(), TimeUnit.SECONDS)
                        } else {
                            Flowable.error(it)
                        }
                    }
            }
            .repeatWhen {
                it.flatMap {
                    Flowable.timer(
                        FILMS_UPDATE_DEFAULT_PERIOD_MILLISECONDS.toLong(),
                        TimeUnit.MILLISECONDS
                    )
                }
            }

    }

    override fun getFilmPreciseDetails(movieId: Int): Single<FilmDetails?> {
        return remoteDataSource.loadFilmPreciseDetails(movieId).map { response ->
            var filmDetails: FilmDetails? = null
            if (response.isSuccessful) {
                response.body()?.let { filmPreciseDetailsJson ->
                    filmDetails = serverResponseToFilmDetailsMapper.map(filmPreciseDetailsJson)
                }
            } else {
                response.errorBody()?.let { message ->
                    throw IOException(message.string())
                }
            }
            filmDetails
        }.retryWhen { throwableFlowable ->
            val counter = AtomicInteger()
            throwableFlowable
                .flatMap {
                    if (it is IOException
                        && counter.getAndIncrement() != FILMS_UPDATE_RETRIES_COUNT
                    ) {
                        Flowable.timer(counter.get().toLong(), TimeUnit.SECONDS)
                    } else {
                        Flowable.error(it)
                    }
                }
        }
    }

    override fun createGuestSession(): Single<String> {
        return remoteDataSource.createGuestSession()
            .map { response ->
                var sessionId = ""
                if (response.isSuccessful) {
                    response.body()?.let {
                        sessionId = it.guestSessionId
                    }
                } else {
                    response.errorBody()?.let { message ->
                        throw IOException(message.string())
                    }
                }
                sessionId
            }
    }

    override fun rateFilm(
        movieId: Int, rate: Float,
        guestSessionId: String
    ): Single<Response<ServerResponse>> {
        return remoteDataSource.rateFilm(movieId, rate, guestSessionId)
    }
}
