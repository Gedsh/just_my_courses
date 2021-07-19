package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pan.alexander.filmrevealer.presentation.Failure

class HomeViewModel : BaseViewModel() {

    val listOfNowPlayingFilmsLiveData by lazy { mainInteractor.get().getNowPlayingFilms() }
    val listOfUpcomingFilmsLiveData by lazy { mainInteractor.get().getUpcomingFilms() }

    fun updateNowPlayingFilms(page: Int) = with(viewModelScope) {
        launch {
            mainInteractor.get().loadNowPlayingFilms(page) { message ->
                mFailureLiveData.value = Failure.WithMessageAndAction(message) {
                    launch {
                        mainInteractor.get().loadNowPlayingFilms(page) {
                            mFailureLiveData.value = Failure.WithMessage(it)
                        }
                    }
                }
            }
        }
    }


    fun updateUpcomingFilms(page: Int) = with(viewModelScope) {
        launch {
            mainInteractor.get().loadUpcomingFilms(page) { message ->
                mFailureLiveData.value = Failure.WithMessageAndAction(message) {
                    launch {
                        mainInteractor.get().loadUpcomingFilms(page) {
                            mFailureLiveData.value = Failure.WithMessage(it)
                        }
                    }
                }
            }
        }
    }
}
