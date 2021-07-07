package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.*
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.presentation.Failure

class FilmDetailsViewModel : ViewModel(), LifecycleObserver {
    private val mainInteractor = App.instance.daggerComponent.getMainInteractor()

    private val mFailureLiveData by lazy { MutableLiveData<Any>() }
    val failureLiveData: LiveData<Any> = mFailureLiveData

    fun getFilmDetailsLiveData(filmId: Int) = mainInteractor.get().getFilmDetailsById(filmId)

    fun getRatedFilmLiveData(filmId: Int) = mainInteractor.get().getRatedFilmById(filmId)

    fun loadFilmDetails(filmId: Int) =
        mainInteractor.get().loadFilmPreciseDetails(filmId) { message ->
            mFailureLiveData.value = Failure.WithMessageAndAction(message) {
                mainInteractor.get().loadFilmPreciseDetails(filmId) {
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
