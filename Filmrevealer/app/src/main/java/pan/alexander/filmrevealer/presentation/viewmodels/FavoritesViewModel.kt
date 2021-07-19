package pan.alexander.filmrevealer.presentation.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import pan.alexander.filmrevealer.presentation.Failure

class FavoritesViewModel : BaseViewModel() {

    val listOfLikedFilmsLiveData by lazy { mainInteractor.get().getLikedFilms() }
    val listOfRatedFilmsLiveData by lazy { mainInteractor.get().getUserRatedFilms() }

    fun updateRatedFilms() = with(viewModelScope) {
        launch {
            val guestSessionId = mainInteractor.get().getUserGuestSessionId().firstOrNull()
            guestSessionId?.takeIf { it.isNotBlank() }?.let { sessionId ->
                mainInteractor.get().loadUserRatedFilms(sessionId) { message ->
                    mFailureLiveData.value = Failure.WithMessageAndAction(message) {
                        launch {
                            mainInteractor.get().loadUserRatedFilms(sessionId) {
                                mFailureLiveData.value = Failure.WithMessage(it)
                            }
                        }
                    }
                }
            }
        }
    }
}
