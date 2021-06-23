package pan.alexander.filmrevealer.data

import android.os.Build
import android.util.Base64
import android.util.Log
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.R
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

    private val key = Base64.decode("${App.instance.getString(R.string.api)}=", Base64.DEFAULT)
        .toString(charset("UTF-8"))

    override fun loadNowPlayingFilms(page: Int): Call<FilmsPage> {
        Log.e(LOG_TAG, "$")
        return api.get().getNowPlaying(
            apiKey = key,
            language = "${locale.language}-${locale.country}",
            page = page,
            region = locale.country
        )
    }

    override fun loadUpcomingFilms(page: Int): Call<FilmsPage> {
        return api.get().getUpcoming(
            apiKey = key,
            language = locale.language,
            page = page,
            region = locale.country
        )
    }

    override fun loadTopRatedFilms(page: Int): Call<FilmsPage> {
        return api.get().getTopRated(
            apiKey = key,
            language = locale.language,
            page = page,
            region = locale.country
        )
    }

    override fun loadPopularFilms(page: Int): Call<FilmsPage> {
        return api.get().getPopular(
            apiKey = key,
            language = locale.language,
            page = page,
            region = locale.country
        )
    }
}
