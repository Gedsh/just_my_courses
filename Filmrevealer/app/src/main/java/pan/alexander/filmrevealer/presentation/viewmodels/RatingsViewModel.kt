package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import pan.alexander.filmrevealer.App

class RatingsViewModel : ViewModel() {

    private val mainInteractor = App.instance.daggerComponent.getMainInteractor()

    val listOfTopRatedFilmsLiveData by lazy { mainInteractor.get().getTopRatedFilms() }
    val listOfPopularFilmsLiveData by lazy { mainInteractor.get().getPopularFilms() }

    fun updateTopRatedFilms(page: Int) {
        mainInteractor.get().loadTopRatedFilms(page)
    }

    fun updatePopularFilms(page: Int) {
        mainInteractor.get().loadPopularFilms(page)
    }
}
