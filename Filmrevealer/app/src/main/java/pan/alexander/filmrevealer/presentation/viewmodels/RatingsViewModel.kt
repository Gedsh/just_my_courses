package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.*
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.presentation.Failure

class RatingsViewModel : ViewModel(), LifecycleObserver {

    private val mainInteractor = App.instance.daggerComponent.getMainInteractor()

    private val mFailureLiveData by lazy { MutableLiveData<Any>() }
    val failureLiveData: LiveData<Any> = mFailureLiveData

    val listOfTopRatedFilmsLiveData by lazy { mainInteractor.get().getTopRatedFilms() }
    val listOfPopularFilmsLiveData by lazy { mainInteractor.get().getPopularFilms() }

    fun updateTopRatedFilms(page: Int) = mainInteractor.get().loadTopRatedFilms(page) { message ->
        mFailureLiveData.value = Failure.WithMessageAndAction(message) {
            mainInteractor.get().loadTopRatedFilms(page) {
                mFailureLiveData.value = Failure.WithMessage(it)
            }
        }
    }

    fun updatePopularFilms(page: Int) = mainInteractor.get().loadPopularFilms(page) { message ->
        mFailureLiveData.value = Failure.WithMessageAndAction(message) {
            mainInteractor.get().loadPopularFilms(page) {
                mFailureLiveData.value = Failure.WithMessage(it)
            }
        }
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

}
