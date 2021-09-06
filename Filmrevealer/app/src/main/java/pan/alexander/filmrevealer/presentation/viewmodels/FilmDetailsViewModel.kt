package pan.alexander.filmrevealer.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import pan.alexander.filmrevealer.presentation.Failure

class FilmDetailsViewModel : BaseViewModel() {

    private var filmDetailsMutableLiveData: MutableLiveData<List<FilmDetails>>? = null
    private var ratedFilmMutableLiveData: MutableLiveData<List<Film>>? = null

    fun getFilmDetailsLiveData(filmId: Int): LiveData<List<FilmDetails>> {
        if (filmDetailsMutableLiveData == null) {
            filmDetailsMutableLiveData = MutableLiveData()

            disposables += mainInteractor.get().getFilmDetailsById(filmId)
                .distinctUntilChanged()
                .subscribeBy(
                    onNext = { filmDetailsMutableLiveData?.value = it },
                    onError = { throwable ->
                        throwable.message?.let {
                            mFailureLiveData.value = Failure.WithMessage(it)
                        }
                        Log.e(LOG_TAG, "Get film details fault", throwable)
                    }
                )
        }
        return filmDetailsMutableLiveData ?: MutableLiveData()
    }

    fun getRatedFilmLiveData(filmId: Int): LiveData<List<Film>> {
        if (ratedFilmMutableLiveData == null) {
            ratedFilmMutableLiveData = MutableLiveData()

            disposables += mainInteractor.get().getRatedFilmById(filmId)
                .distinctUntilChanged()
                .subscribeBy(
                    onNext = { ratedFilmMutableLiveData?.value = it },
                    onError = { throwable ->
                        throwable.message?.let {
                            mFailureLiveData.value = Failure.WithMessage(it)
                        }
                        Log.e(LOG_TAG, "Get film rating fault", throwable)
                    }
                )
        }
        return ratedFilmMutableLiveData ?: MutableLiveData()
    }

    fun loadFilmDetails(filmId: Int) {
        disposables += mainInteractor.get().loadFilmPreciseDetails(filmId)
            .subscribeBy(
                onError = { throwable ->
                    throwable.message?.let {
                        mFailureLiveData.value = Failure.WithMessage(it)
                    }
                    Log.e(LOG_TAG, "Load film precise details fault", throwable)
                }
            )
    }

}
