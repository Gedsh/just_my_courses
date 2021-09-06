package pan.alexander.filmrevealer.services

import android.app.Service
import android.content.Intent
import android.content.Context
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.R
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.interactors.MainInteractor
import pan.alexander.filmrevealer.presentation.fragments.FilmDetailsFragment
import java.lang.RuntimeException
import javax.inject.Inject

private const val ACTION_RATE_FILM = "pan.alexander.filmrevealer.services.action.RATE_FILM"

private const val EXTRA_PARAM_FILM = "pan.alexander.filmrevealer.services.extra.PARAM_FILM"
private const val EXTRA_PARAM_RATING = "pan.alexander.filmrevealer.services.extra.PARAM_RATING"

@ExperimentalCoroutinesApi
class RateFilmIntentService : Service() {

    @Inject
    lateinit var mainInteractor: dagger.Lazy<MainInteractor>

    private val disposables by lazy { CompositeDisposable() }

    override fun onCreate() {
        super.onCreate()

        App.instance.daggerComponent.inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_RATE_FILM -> {
                val film = intent.getParcelableExtra<Film>(EXTRA_PARAM_FILM)
                val rate = intent.getFloatExtra(EXTRA_PARAM_RATING, .0f)
                film?.let {
                    if (rate > 0) {
                        rateFilm(it, rate, startId)
                    }
                }
            }
            else -> stopSelf(startId)
        }
        return START_NOT_STICKY
    }

    @ExperimentalCoroutinesApi
    private fun rateFilm(film: Film, rate: Float, taskId: Int) {
        disposables += mainInteractor.get().getUserGuestSessionId()
            .flatMapCompletable {
                if (it.isEmpty()) {
                    mainInteractor.get().createGuestSession()
                        .andThen(mainInteractor.get().getUserGuestSessionId())
                        .flatMap { sessionId ->
                            if (sessionId.isEmpty()) {
                                Single.error(RuntimeException(getString(R.string.create_guest_session_failure)))
                            } else {
                                Single.just(sessionId)
                            }
                        }.flatMapCompletable { sessionId ->
                            mainInteractor.get().rateFilm(
                                film = film,
                                rate = rate,
                                guestSessionId = sessionId
                            )
                        }
                } else {
                    mainInteractor.get().rateFilm(
                        film = film,
                        rate = rate,
                        guestSessionId = it
                    )
                }
            }.subscribeBy(
                onError = {
                    sendBroadcast(it.message ?: getString(R.string.create_guest_session_failure))
                    Log.e(LOG_TAG, "Rate ${film.title} film failure", it)
                    stopSelf(taskId)
                },
                onComplete = { stopSelf(taskId) }
            )
    }

    private fun sendBroadcast(message: String) {
        val intent = Intent(FilmDetailsFragment.RATE_FILM_ERROR_ACTION).apply {
            putExtra(FilmDetailsFragment.EXTRA_PARAM_ERROR, message)
        }
        LocalBroadcastManager.getInstance(App.instance).sendBroadcast(intent)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

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
