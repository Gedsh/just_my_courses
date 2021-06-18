package pan.alexander.filmrevealer.data

import android.os.Build
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.data.web.pojo.FilmsPage
import pan.alexander.filmrevealer.domain.RemoteRepository
import retrofit2.Call

class RemoteRepositoryImplementation : RemoteRepository {
    private val api = App.instance.daggerComponent.getFilmsApiService()

    @Suppress("DEPRECATION")
    private val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        App.instance.resources.configuration.locales[0]
    } else {
        App.instance.resources.configuration.locale
    }
    private val apiKey = "API_KEY" //TODO: replace with real

    override fun loadNowPlayingFilms(page: Int): Call<FilmsPage> {
        return api.get().getNowPlaying(
            apiKey = apiKey,
            language = locale.language,
            page = page,
            region = locale.country
        )
    }

    override fun loadUpcomingFilms(page: Int): Call<FilmsPage> {
        return api.get().getUpcoming(
            apiKey = apiKey,
            language = locale.language,
            page = page,
            region = locale.country
        )
    }

    override fun loadTopRatedFilms(page: Int): Call<FilmsPage> {
        return api.get().getTopRated(
            apiKey = apiKey,
            language = locale.language,
            page = page,
            region = locale.country
        )
    }

    override fun loadPopularFilms(page: Int): Call<FilmsPage> {
        return api.get().getPopular(
            apiKey = apiKey,
            language = locale.language,
            page = page,
            region = locale.country
        )
    }
}
