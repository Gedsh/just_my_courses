package pan.alexander.filmrevealer.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.Failure
import java.util.concurrent.TimeUnit

class HomeViewModel : BaseViewModel() {

    private var nowPlayingDisposable: Disposable? = null
    private var upcomingDisposable: Disposable? = null

    val listOfNowPlayingFilmsLiveData: LiveData<List<Film>> by lazy {
        val filmsMutableLiveData = MutableLiveData<List<Film>>()
        disposables += mainInteractor.get().getNowPlayingFilms()
            .distinctUntilChanged()
            .subscribeBy(
                onNext = { filmsMutableLiveData.value = it },
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Get now playing films fault", throwable)
                }
            )
        filmsMutableLiveData
    }

    val listOfUpcomingFilmsLiveData by lazy {
        val filmsMutableLiveData = MutableLiveData<List<Film>>()
        disposables += mainInteractor.get().getUpcomingFilms()
            .distinctUntilChanged()
            .subscribeBy(
                onNext = { filmsMutableLiveData.value = it },
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Get upcoming films fault", throwable)
                }
            )
        filmsMutableLiveData
    }

    private var nowPlayingFilmsUpdateRequest: ((page: Int) -> Unit)? = null

    init {
        disposables += Observable.create<Int> { emitter ->
            nowPlayingFilmsUpdateRequest = { emitter.onNext(it) }
        }.distinctUntilChanged()
            .debounce(1, TimeUnit.SECONDS)
            .subscribeBy(
                onNext = { page ->
                    sendNowPlayingFilmsUpdateRequest(page)
                }
            )
    }

    private fun sendNowPlayingFilmsUpdateRequest(page: Int) {
        nowPlayingDisposable?.dispose()
        nowPlayingDisposable = mainInteractor.get().loadNowPlayingFilms(page)
            .subscribeBy(
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Load now playing films fault", throwable)
                }
            )
    }

    fun updateNowPlayingFilms(page: Int) {
        nowPlayingFilmsUpdateRequest?.invoke(page)
    }

    private var upcomingFilmsUpdateRequest: ((page: Int) -> Unit)? = null

    init {
        disposables += Observable.create<Int> { emitter ->
            upcomingFilmsUpdateRequest = { emitter.onNext(it) }
        }.distinctUntilChanged()
            .debounce(1, TimeUnit.SECONDS)
            .subscribeBy(
                onNext = { page ->
                    sendUpcomingFilmsUpdateRequest(page)
                }
            )
    }

    private fun sendUpcomingFilmsUpdateRequest(page: Int) {
        upcomingDisposable?.dispose()
        upcomingDisposable = mainInteractor.get().loadUpcomingFilms(page)
            .subscribeBy(
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Load upcoming films fault", throwable)
                }
            )
    }

    fun updateUpcomingFilms(page: Int) {
        upcomingFilmsUpdateRequest?.invoke(page)
    }

    override fun onCleared() {
        nowPlayingDisposable?.dispose()
        upcomingDisposable?.dispose()

        super.onCleared()
    }
}
