package pan.alexander.filmrevealer.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.Failure
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class FavoritesViewModel : BaseViewModel() {

    private var ratedDisposable: Disposable? = null

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

    private var ratedFilmsUpdateRequest: (() -> Unit)? = null

    init {
        disposables += Observable.create<Int> { emitter ->
            ratedFilmsUpdateRequest = { emitter.onNext(0) }
        }.distinctUntilChanged()
            .debounce(1, TimeUnit.SECONDS)
            .subscribeBy(
                onNext = {
                    sendRatedFilmsUpdateRequest()
                }
            )
    }

    @ExperimentalCoroutinesApi
    private fun sendRatedFilmsUpdateRequest() {
        ratedDisposable?.dispose()
        ratedDisposable = mainInteractor.get().getUserGuestSessionId()
            .filter { it.isNotEmpty() }
            .flatMapCompletable { mainInteractor.get().loadUserRatedFilms(it) }
            .subscribeBy(
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(LOG_TAG, "Load user rated films fault", throwable)
                }
            )
    }

    fun updateRatedFilms() {
        ratedFilmsUpdateRequest?.invoke()
    }

    override fun onCleared() {
        ratedDisposable?.dispose()

        super.onCleared()
    }
}
