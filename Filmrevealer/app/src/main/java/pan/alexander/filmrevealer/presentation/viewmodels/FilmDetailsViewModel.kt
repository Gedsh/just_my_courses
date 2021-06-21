package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import pan.alexander.filmrevealer.App

class FilmDetailsViewModel : ViewModel() {
    private val mainInteractor = App.instance.daggerComponent.getMainInteractor()

    fun getFilmDetailsLiveData(filmId: Int) = mainInteractor.get().getFilmDetailsById(filmId)

    fun loadFilmDetails(filmId: Int) = mainInteractor.get().loadFilmPreciseDetails(filmId)

}
