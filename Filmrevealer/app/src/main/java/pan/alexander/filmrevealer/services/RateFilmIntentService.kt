package pan.alexander.filmrevealer.services

import android.app.IntentService
import android.content.Intent
import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.R
import pan.alexander.filmrevealer.domain.MainInteractor
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.fragments.FilmDetailsFragment

private const val ACTION_RATE_FILM = "pan.alexander.filmrevealer.services.action.RATE_FILM"

private const val EXTRA_PARAM_FILM = "pan.alexander.filmrevealer.services.extra.PARAM_FILM"
private const val EXTRA_PARAM_RATING = "pan.alexander.filmrevealer.services.extra.PARAM_RATING"

class RateFilmIntentService : IntentService("RateFilmIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_RATE_FILM -> {
                val film = intent.getParcelableExtra<Film>(EXTRA_PARAM_FILM)
                val rate = intent.getFloatExtra(EXTRA_PARAM_RATING, .0f)
                film?.let {
                    if (rate > 0) {
                        rateFilm(it, rate)
                    }
                }
            }
        }
    }

    private fun rateFilm(film: Film, rate: Float) = runBlocking {
        val mainInteractor = App.instance.daggerComponent.getMainInteractor().get()
        var guestSessionId = mainInteractor.getUserGuestSessionId().firstOrNull()

        if (guestSessionId.isNullOrBlank()) {
            createGuestSession(mainInteractor)
            guestSessionId = mainInteractor.getUserGuestSessionId().firstOrNull()
        }

        if (!guestSessionId.isNullOrBlank()) {
            mainInteractor.rateFilm(film, rate, guestSessionId) { message ->
                sendBroadcast(message)
            }
        } else {
            sendBroadcast(App.instance.getString(R.string.create_guest_session_failure))
        }
    }

    private suspend fun createGuestSession(mainInteractor: MainInteractor) {
        mainInteractor.createGuestSession { message ->
            sendBroadcast(message)
        }
    }

    private fun sendBroadcast(message: String) {
        val intent = Intent(FilmDetailsFragment.RATE_FILM_ERROR_ACTION).apply {
            putExtra(FilmDetailsFragment.EXTRA_PARAM_ERROR, message)
        }
        LocalBroadcastManager.getInstance(App.instance).sendBroadcast(intent)
    }

    companion object {
        @JvmStatic
        fun rateFilm(context: Context, film: Film, rate: Float) {
            val intent = Intent(context, RateFilmIntentService::class.java).apply {
                action = ACTION_RATE_FILM
                putExtra(EXTRA_PARAM_FILM, film)
                putExtra(EXTRA_PARAM_RATING, rate)
            }
            context.startService(intent)
        }
    }
}
