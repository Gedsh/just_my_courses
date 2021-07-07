package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pan.alexander.filmrevealer.presentation.Failure

class FilmDetailsViewModel : BaseViewModel() {

    fun getFilmDetailsLiveData(filmId: Int) = mainInteractor.get().getFilmDetailsById(filmId)

    fun getRatedFilmLiveData(filmId: Int) = mainInteractor.get().getRatedFilmById(filmId)

    fun loadFilmDetails(filmId: Int) = with(viewModelScope) {
        launch {
            mainInteractor.get().loadFilmPreciseDetails(filmId) { message ->
                mFailureLiveData.value = Failure.WithMessageAndAction(message) {
                    launch {
                        mainInteractor.get().loadFilmPreciseDetails(filmId) {
                            mFailureLiveData.value = Failure.WithMessage(it)
                        }
                    }
                }
            }
        }
    }

}
