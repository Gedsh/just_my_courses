package pan.alexander.filmrevealer.data

import android.os.Build
import android.util.Base64
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.BuildConfig
import pan.alexander.filmrevealer.data.web.pojo.*
import pan.alexander.filmrevealer.domain.RemoteRepository
import retrofit2.Response

class RemoteRepositoryImplementation : RemoteRepository {
    private val api = App.instance.daggerComponent.getFilmsApiService()

    @Suppress("DEPRECATION")
    private val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        App.instance.resources.configuration.locales[0]
    } else {
        App.instance.resources.configuration.locale
    }

    var region: String = locale.country.uppercase()

    private val key = Base64.decode("${BuildConfig.API_KEY}=", Base64.DEFAULT)
        .toString(charset("UTF-8"))

    override suspend fun loadNowPlayingFilms(page: Int): Response<FilmsPageJson> {
        return api.get().getNowPlaying(
            apiKey = key,
            language = "${locale.language}-${locale.country}",
            page = page,
            region = region
        )
    }

    override suspend fun loadUpcomingFilms(page: Int): Response<FilmsPageJson> {
        return api.get().getUpcoming(
            apiKey = key,
            language = "${locale.language}-${locale.country}",
            page = page,
            region = region
        )
    }

    override suspend fun loadTopRatedFilms(page: Int): Response<FilmsPageJson> {
        return api.get().getTopRated(
            apiKey = key,
            language = "${locale.language}-${locale.country}",
            page = page,
            region = region
        )
    }

    override suspend fun loadPopularFilms(page: Int): Response<FilmsPageJson> {
        return api.get().getPopular(
            apiKey = key,
            language = "${locale.language}-${locale.country}",
            page = page,
            region = region
        )
    }

    override suspend fun loadFilmPreciseDetails(movieId: Int): Response<FilmPreciseDetailsJson> {
        return api.get().getPreciseDetails(
            movieId = movieId,
            apiKey = key,
            language = "${locale.language}-${locale.country}"
        )
    }

    override suspend fun createGuestSession(): Response<GuestSession> {
        return api.get().createGuestSession(
            apiKey = key
        )
    }

    override suspend fun getUserRatedFilms(guestSessionId: String): Response<FilmsPageJson> {
        return api.get().getRatedByUser(
            guestSessionId = guestSessionId,
            apiKey = key,
            language = "${locale.language}-${locale.country}"
        )
    }

    override suspend fun rateFilm(
        movieId: Int,
        rate: Float,
        guestSessionId: String
    ): Response<ServerResponse> {
        return api.get().rateFilm(
            movieId = movieId,
            body = RateBody(rate),
            apiKey = key,
            guestSessionId = guestSessionId
        )
    }

}
