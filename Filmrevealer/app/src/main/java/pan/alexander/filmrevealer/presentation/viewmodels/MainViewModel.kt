package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pan.alexander.filmrevealer.App

class MainViewModel : ViewModel(), LifecycleObserver {
    private val mainIntIterator = App.instance.daggerComponent.getMainInteractor()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun cleanDatabase() = viewModelScope.launch {
        mainIntIterator.get().deleteOldFilmDetails()
    }
}
