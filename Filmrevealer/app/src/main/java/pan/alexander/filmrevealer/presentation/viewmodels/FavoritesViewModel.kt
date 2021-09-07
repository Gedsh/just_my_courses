package pan.alexander.filmrevealer.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.Failure
import pan.alexander.filmrevealer.utils.NETWORK_REQUEST_THROTTLE_TIME_MSEC
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class FavoritesViewModel : BaseViewModel() {

    val listOfLikedFilmsLiveData: LiveData<List<Film>> by lazy {
        val filmsMutableLiveData = MutableLiveData<List<Film>>()
        disposables += mainInteractor.get().getLikedFilms()
            .subscribeBy(
                onNext = { filmsMutableLiveData.value = it },
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(LOG_TAG, "Get liked films fault", throwable)
                }
            )
        filmsMutableLiveData
    }

    val listOfRatedFilmsLiveData: LiveData<List<Film>> by lazy {
        val filmsMutableLiveData = MutableLiveData<List<Film>>()
        disposables += mainInteractor.get().getUserRatedFilms()
            .subscribeBy(
                onNext = { filmsMutableLiveData.value = it },
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(LOG_TAG, "Get user rated films fault", throwable)
                }
            )
        filmsMutableLiveData
    }

    fun updateRatedFilms() {
        ratedFilmsUpdateRequest?.invoke()
    }

    private var ratedFilmsUpdateRequest: (() -> Unit)? = null

    init {
        disposables += Observable.create<Int> { emitter ->
            ratedFilmsUpdateRequest = { emitter.onNext(0) }
        }
            .throttleFirst(NETWORK_REQUEST_THROTTLE_TIME_MSEC, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMapCompletable {
                mainInteractor.get().getUserGuestSessionId()
                    .filter { it.isNotEmpty() }
                    .flatMapCompletable { mainInteractor.get().loadUserRatedFilms(it) }
            }
            .subscribeBy(
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(LOG_TAG, "Load user rated films fault", throwable)
                }
            )
    }
}
