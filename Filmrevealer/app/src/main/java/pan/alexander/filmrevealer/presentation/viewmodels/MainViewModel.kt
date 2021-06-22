package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import pan.alexander.filmrevealer.App

class MainViewModel : ViewModel(), LifecycleObserver {
    private val mainIntIterator = App.instance.daggerComponent.getMainInteractor()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun cleanDatabase() {
        mainIntIterator.get().deleteOldFilmDetails()
    }
}
