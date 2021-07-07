package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.presentation.Failure

open class BaseViewModel : ViewModel(), LifecycleObserver {
    protected val mainInteractor = App.instance.daggerComponent.getMainInteractor()

    val listOfLikedImdbIdsLiveData by lazy { mainInteractor.get().getLikedImdbIds() }

    protected val mFailureLiveData by lazy { MutableLiveData<Any>() }
    val failureLiveData: LiveData<Any> = mFailureLiveData

    fun toggleFilmLike(film: Film) = viewModelScope.launch {
        mainInteractor.get().toggleLike(film)
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
