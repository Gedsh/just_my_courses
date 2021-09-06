package pan.alexander.filmrevealer.data.remote

import io.reactivex.rxjava3.core.Single
import pan.alexander.filmrevealer.data.remote.pojo.*
import pan.alexander.filmrevealer.utils.configuration.ConfigurationManager
import pan.alexander.filmrevealer.utils.locales.LocaleManager
import pan.alexander.filmrevealer.web.FilmsApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val api: FilmsApiService,
    configurationManager: ConfigurationManager,
    localeManager: LocaleManager
): RemoteDataSource {

    private val apiKey = configurationManager.getApiKey()
    private val language = localeManager.getLanguage()
    private val region = localeManager.getRegion()

    override fun loadNowPlayingFilms(page: Int): Single<Response<FilmsPageJson>> {
        return api.getNowPlaying(
            apiKey = apiKey,
            language = language,
            page = page,
            region = region
        )
    }

    override fun loadUpcomingFilms(page: Int): Single<Response<FilmsPageJson>> {
        return api.getUpcoming(
            apiKey = apiKey,
            language = language,
            page = page,
            region = region
        )
    }

    override fun loadTopRatedFilms(page: Int): Single<Response<FilmsPageJson>> {
        return api.getTopRated(
            apiKey = apiKey,
            language = language,
            page = page,
            region = region
        )
    }

    override fun loadPopularFilms(page: Int): Single<Response<FilmsPageJson>> {
        return api.getPopular(
            apiKey = apiKey,
            language = language,
            page = page,
            region = region
        )
    }

    override fun loadFilmPreciseDetails(movieId: Int): Single<Response<FilmPreciseDetailsJson>> {
        return api.getPreciseDetails(
            movieId = movieId,
            apiKey = apiKey,
            language = language
        )
    }

    override fun createGuestSession(): Single<Response<GuestSession>> {
        return api.createGuestSession(
            apiKey = apiKey
        )
    }

    override fun getUserRatedFilms(guestSessionId: String): Single<Response<FilmsPageJson>> {
        return api.getRatedByUser(
            guestSessionId = guestSessionId,
            apiKey = apiKey,
            language = language
        )
    }

    override fun rateFilm(
        movieId: Int,
        rate: Float,
        guestSessionId: String
    ): Single<Response<ServerResponse>> {
        return api.rateFilm(
            movieId = movieId,
            body = RateBody(rate),
            apiKey = apiKey,
            guestSessionId = guestSessionId
        )
    }
}
