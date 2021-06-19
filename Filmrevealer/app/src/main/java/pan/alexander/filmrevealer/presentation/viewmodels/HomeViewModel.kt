package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.domain.entities.Film

class HomeViewModel : ViewModel() {

    private val mainInteractor = App.instance.daggerComponent.getMainInteractor()
    private val mListOfNowPlayingFilmsLiveData by lazy { mainInteractor.get().getNowPlayingFilms() }
    private val mListOfUpcomingFilmsLiveData by lazy { mainInteractor.get().getUpcomingFilms() }

    val listOfNowPlayingFilmsLiveData: LiveData<List<Film>> = mListOfNowPlayingFilmsLiveData
    val listOfUpcomingFilmsLiveData: LiveData<List<Film>> = mListOfUpcomingFilmsLiveData

    fun updateNowPlayingFilms(page: Int) {
        mainInteractor.get().loadNowPlayingFilms(page)
    }

    fun updateUpcomingFilms(page: Int) {
        mainInteractor.get().loadUpcomingFilms(page)
    }
}
