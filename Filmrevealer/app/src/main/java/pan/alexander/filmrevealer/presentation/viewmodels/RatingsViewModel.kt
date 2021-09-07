package pan.alexander.filmrevealer.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.Failure
import pan.alexander.filmrevealer.utils.NETWORK_REQUEST_THROTTLE_TIME_MSEC
import java.util.concurrent.TimeUnit

class RatingsViewModel : BaseViewModel() {

    val listOfTopRatedFilmsLiveData: LiveData<List<Film>> by lazy {
        val filmsMutableLiveData = MutableLiveData<List<Film>>()
        disposables += mainInteractor.get().getTopRatedFilms()
            .distinctUntilChanged()
            .subscribeBy(
                onNext = { filmsMutableLiveData.value = it },
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Get top rated films fault", throwable)
                }
            )
        filmsMutableLiveData
    }

    val listOfPopularFilmsLiveData: LiveData<List<Film>> by lazy {
        val filmsMutableLiveData = MutableLiveData<List<Film>>()
        disposables += mainInteractor.get().getPopularFilms()
            .distinctUntilChanged()
            .subscribeBy(
                onNext = { filmsMutableLiveData.value = it },
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Get popular films fault", throwable)
                }
            )
        filmsMutableLiveData
    }

    fun updateTopRatedFilms(page: Int) {
        topRatedFilmsUpdateRequest?.invoke(page)
    }

    private var topRatedFilmsUpdateRequest: ((page: Int) -> Unit)? = null

    init {
        disposables += Observable.create<Int> { emitter ->
            topRatedFilmsUpdateRequest = { emitter.onNext(it) }
        }
            .throttleFirst(NETWORK_REQUEST_THROTTLE_TIME_MSEC, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMapCompletable {
                mainInteractor.get().loadTopRatedFilms(it)
            }
            .subscribeBy(
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Load top rated films fault", throwable)
                }
            )
    }

    fun updatePopularFilms(page: Int) {
        popularFilmsUpdateRequest?.invoke(page)
    }

    private var popularFilmsUpdateRequest: ((page: Int) -> Unit)? = null

    init {
        disposables += Observable.create<Int> { emitter ->
            popularFilmsUpdateRequest = { emitter.onNext(it) }
        }
            .throttleFirst(NETWORK_REQUEST_THROTTLE_TIME_MSEC, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMapCompletable {
                mainInteractor.get().loadPopularFilms(it)
            }
            .subscribeBy(
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Load popular films fault", throwable)
                }
            )
    }
}
