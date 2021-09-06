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

class RatingsViewModel : BaseViewModel() {

    private var topRatedDisposable: Disposable? = null
    private var popularDisposable: Disposable? = null

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

    private var topRatedFilmsUpdateRequest: ((page: Int) -> Unit)? = null

    init {
        disposables += Observable.create<Int> { emitter ->
            topRatedFilmsUpdateRequest = { emitter.onNext(it) }
        }.distinctUntilChanged()
            .debounce(1, TimeUnit.SECONDS)
            .subscribeBy(
                onNext = { page ->
                    sendTopRatedFilmsUpdateRequest(page)
                }
            )
    }

    private fun sendTopRatedFilmsUpdateRequest(page: Int) {
        topRatedDisposable?.dispose()
        topRatedDisposable = mainInteractor.get().loadTopRatedFilms(page)
            .subscribeBy(
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Load top rated films fault", throwable)
                }
            )
    }

    fun updateTopRatedFilms(page: Int) {
        topRatedFilmsUpdateRequest?.invoke(page)
    }

    private var popularFilmsUpdateRequest: ((page: Int) -> Unit)? = null

    init {
        disposables += Observable.create<Int> { emitter ->
            popularFilmsUpdateRequest = { emitter.onNext(it) }
        }.distinctUntilChanged()
            .debounce(1, TimeUnit.SECONDS)
            .subscribeBy(
                onNext = { page ->
                    sendPopularFilmsUpdateRequest(page)
                }
            )
    }

    private fun sendPopularFilmsUpdateRequest(page: Int) {
        popularDisposable?.dispose()
        popularDisposable = mainInteractor.get().loadPopularFilms(page)
            .subscribeBy(
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Load popular films fault", throwable)
                }
            )
    }

    fun updatePopularFilms(page: Int) {
        popularFilmsUpdateRequest?.invoke(page)
    }

    override fun onCleared() {
        topRatedDisposable?.dispose()
        popularDisposable?.dispose()

        super.onCleared()
    }
}
