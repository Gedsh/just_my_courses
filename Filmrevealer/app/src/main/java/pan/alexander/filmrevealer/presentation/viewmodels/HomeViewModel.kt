package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import pan.alexander.filmrevealer.App

class HomeViewModel : ViewModel() {

    private val mainInteractor = App.instance.daggerComponent.getMainInteractor()

    val listOfNowPlayingFilmsLiveData by lazy { mainInteractor.get().getNowPlayingFilms() }
    val listOfUpcomingFilmsLiveData by lazy { mainInteractor.get().getUpcomingFilms() }

    fun updateNowPlayingFilms(page: Int) {
        mainInteractor.get().loadNowPlayingFilms(page)
    }

    fun updateUpcomingFilms(page: Int) {
        mainInteractor.get().loadUpcomingFilms(page)
    }
}
