package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import pan.alexander.filmrevealer.App

class MainViewModel : ViewModel() {
    private val mainIntIterator = App.instance.daggerComponent.getMainInteractor()

    override fun onCleared() {
        mainIntIterator.get().deleteOldFilmDetails()
        super.onCleared()
    }
}
