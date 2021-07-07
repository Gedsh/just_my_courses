package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pan.alexander.filmrevealer.presentation.Failure

class RatingsViewModel : BaseViewModel() {

    val listOfTopRatedFilmsLiveData by lazy { mainInteractor.get().getTopRatedFilms() }
    val listOfPopularFilmsLiveData by lazy { mainInteractor.get().getPopularFilms() }

    fun updateTopRatedFilms(page: Int) = with(viewModelScope) {
        launch {
            mainInteractor.get().loadTopRatedFilms(page) { message ->
                mFailureLiveData.value = Failure.WithMessageAndAction(message) {
                    launch {
                        mainInteractor.get().loadTopRatedFilms(page) {
                            mFailureLiveData.value = Failure.WithMessage(it)
                        }
                    }
                }
            }
        }
    }

    fun updatePopularFilms(page: Int) = with(viewModelScope) {
        launch {
            mainInteractor.get().loadPopularFilms(page) { message ->
                mFailureLiveData.value = Failure.WithMessageAndAction(message) {
                    launch {
                        mainInteractor.get().loadPopularFilms(page) {
                            mFailureLiveData.value = Failure.WithMessage(it)
                        }
                    }
                }
            }
        }
    }
}
