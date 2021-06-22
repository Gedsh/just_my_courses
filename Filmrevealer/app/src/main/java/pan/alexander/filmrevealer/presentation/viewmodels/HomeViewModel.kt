package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.*
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.presentation.Failure

class HomeViewModel : ViewModel(), LifecycleObserver {

    private val mainInteractor = App.instance.daggerComponent.getMainInteractor()

    private val mFailureLiveData by lazy { MutableLiveData<Any>() }
    val failureLiveData: LiveData<Any> = mFailureLiveData

    val listOfNowPlayingFilmsLiveData by lazy { mainInteractor.get().getNowPlayingFilms() }
    val listOfUpcomingFilmsLiveData by lazy { mainInteractor.get().getUpcomingFilms() }

    fun updateNowPlayingFilms(page: Int) =
        mainInteractor.get().loadNowPlayingFilms(page) { message ->
            mFailureLiveData.value = Failure.WithMessageAndAction(message) {
                mainInteractor.get().loadNowPlayingFilms(page) {
                    mFailureLiveData.value = Failure.WithMessage(it)
                }
            }
        }

    fun updateUpcomingFilms(page: Int) = mainInteractor.get().loadUpcomingFilms(page) { message ->
        mFailureLiveData.value = Failure.WithMessageAndAction(message) {
            mainInteractor.get().loadUpcomingFilms(page) {
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
