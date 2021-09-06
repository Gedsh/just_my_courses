package pan.alexander.filmrevealer.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.Failure

open class BaseViewModel : ViewModel(), LifecycleObserver {
    protected val mainInteractor = App.instance.daggerComponent.getMainInteractor()
    protected val disposables by lazy { CompositeDisposable() }

    val listOfLikedImdbIdsLiveData: LiveData<List<Int>> by lazy {
        val idsMutableLiveData = MutableLiveData<List<Int>>()
        disposables += mainInteractor.get().getLikedImdbIds()
            .subscribeBy(
                onNext = { idsMutableLiveData.value = it },
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Get liked IMDb ids fault", throwable)
                }
            )
        idsMutableLiveData
    }

    protected val mFailureLiveData by lazy { MutableLiveData<Any>() }
    val failureLiveData: LiveData<Any> = mFailureLiveData

    fun toggleFilmLike(film: Film) {
        disposables += mainInteractor.get().toggleLike(film)
            .subscribeBy(
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(App.LOG_TAG, "Toggle ${film.title} film like fault", throwable)
                }
            )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        clearLastFailure()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        clearLastFailure()
    }

    private fun clearLastFailure() {
        mFailureLiveData.value = Failure.WithMessage("")
    }

    override fun onCleared() {
        disposables.clear()

        super.onCleared()
    }
}
