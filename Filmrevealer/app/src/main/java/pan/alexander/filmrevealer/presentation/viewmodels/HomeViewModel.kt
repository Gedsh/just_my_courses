package pan.alexander.filmrevealer.presentation.viewmodels

import pan.alexander.filmrevealer.presentation.Failure

class HomeViewModel : BaseViewModel() {

    val listOfNowPlayingFilmsLiveData by lazy { mainInteractor.get().getNowPlayingFilms() }
    val listOfUpcomingFilmsLiveData by lazy { mainInteractor.get().getUpcomingFilms() }

    fun updateNowPlayingFilms(page: Int) =
        mainInteractor.get().loadNowPlayingFilms(page) { message ->
            mFailureLiveData.value = Failure.WithMessageAndAction(message) {
                mainInteractor.get().loadNowPlayingFilms(page) {
                    mFailureLiveData.value = Failure.WithMessage(it)
                }
            }
        }

    fun updateUpcomingFilms(page: Int) = mainInteractor.get().loadUpcomingFilms(page) { message ->
        mFailureLiveData.value = Failure.WithMessageAndAction(message) {
            mainInteractor.get().loadUpcomingFilms(page) {
                mFailureLiveData.value = Failure.WithMessage(it)
            }
        }
    }
}
