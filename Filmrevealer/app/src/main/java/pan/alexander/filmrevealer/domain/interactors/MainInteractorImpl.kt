package pan.alexander.filmrevealer.domain.interactors

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails
import pan.alexander.filmrevealer.domain.local.LocalRepository
import pan.alexander.filmrevealer.domain.preferences.PreferencesRepository
import pan.alexander.filmrevealer.domain.remote.RemoteRepository
import pan.alexander.filmrevealer.utils.FILM_DETAILS_EXPIRE_PERIOD_MILLISECONDS
import java.io.IOException
import javax.inject.Inject

class MainInteractorImpl @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val preferencesRepository: PreferencesRepository
) : MainInteractor {
    override fun loadNowPlayingFilms(page: Int): Completable {
        return remoteRepository.getNowPlayingFilms(page)
            .onBackpressureLatest()
            .filter { it.isNotEmpty() }
            .flatMapCompletable { films ->
                localRepository.deleteNowPlayingFilms(page)
                    .andThen(localRepository.addFilms(films))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadUpcomingFilms(page: Int): Completable {
        return remoteRepository.getUpcomingFilms(page)
            .onBackpressureLatest()
            .filter { it.isNotEmpty() }
            .flatMapCompletable { films ->
                localRepository.deleteUpcomingFilms(page)
                    .andThen(localRepository.addFilms(films))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadTopRatedFilms(page: Int): Completable {
        return remoteRepository.getTopRatedFilms(page)
            .onBackpressureLatest()
            .filter { it.isNotEmpty() }
            .flatMapCompletable { films ->
                localRepository.deleteTopRatedFilms(page)
                    .andThen(localRepository.addFilms(films))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadPopularFilms(page: Int): Completable {
        return remoteRepository.getPopularFilms(page)
            .onBackpressureLatest()
            .filter { it.isNotEmpty() }
            .flatMapCompletable { films ->
                localRepository.deletePopularFilms(page)
                    .andThen(localRepository.addFilms(films))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadUserRatedFilms(guestSessionId: String): Completable {
        return remoteRepository.getUserRatedFilms(guestSessionId)
            .onBackpressureLatest()
            .filter { it.isNotEmpty() }
            .flatMapCompletable { films ->
                localRepository.deleteUserRatedFilms(films.first().page)
                    .andThen(localRepository.addFilms(films))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadFilmPreciseDetails(movieId: Int): Completable {
        return remoteRepository.getFilmPreciseDetails(movieId)
            .flatMapCompletable { filmDetails ->
                localRepository.getFilmDetailsById(movieId)
                    .firstElement()
                    .flatMapCompletable {
                        when {
                            filmDetails == null -> Completable.complete()
                            it.isEmpty() -> localRepository.addFilmDetails(filmDetails)
                            else -> localRepository.updateFilmDetails(filmDetails)
                        }
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    @ExperimentalCoroutinesApi
    override fun createGuestSession(): Completable {
        return remoteRepository.createGuestSession()
            .flatMapCompletable {
                preferencesRepository.setGuestSessionIdRx(it)
            }
    }

    override fun rateFilm(film: Film, rate: Float, guestSessionId: String): Completable {
        return remoteRepository.rateFilm(
            movieId = film.movieId,
            rate = rate,
            guestSessionId = guestSessionId
        ).flatMapCompletable { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    localRepository.addFilm(film)
                }
            } else {
                response.errorBody()?.let { message ->
                    throw IOException(message.string())
                }
            }
        }
    }

    override fun getNowPlayingFilms(): Flowable<List<Film>> {
        return localRepository.getNowPlayingFilms()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUpcomingFilms(): Flowable<List<Film>> {
        return localRepository.getUpcomingFilms()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTopRatedFilms(): Flowable<List<Film>> {
        return localRepository.getTopRatedFilms()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getPopularFilms(): Flowable<List<Film>> {
        return localRepository.getPopularFilms()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserRatedFilms(): Flowable<List<Film>> {
        return localRepository.getUserRatedFilms()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getLikedFilms(): Flowable<List<Film>> {
        return localRepository.getLikedFilms()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getLikedImdbIds(): Flowable<List<Int>> {
        return localRepository.getLikedImdbIds()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getRatedFilmById(movieId: Int): Flowable<List<Film>> {
        return localRepository.getRatedFilmById(movieId)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    @ExperimentalCoroutinesApi
    override fun getUserGuestSessionId(): Single<String> {
        return preferencesRepository.getGuestSessionIdRx()
            .subscribeOn(Schedulers.io())
    }

    override fun toggleLike(film: Film): Completable {
        return localRepository.getLikedFilmById(film.movieId)
            .flatMapCompletable {
                if (it.isEmpty()) {
                    localRepository.addFilm(
                        film.apply {
                            id = 0
                            page = 1
                            totalPages = 1
                            section = Film.Section.LIKED.value
                        }
                    )
                } else {
                    localRepository.deleteFilm(it.first())
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFilmDetailsById(id: Int): Flowable<List<FilmDetails>> {
        return localRepository.getFilmDetailsById(id)
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteOldFilmDetails(): Completable {
        return localRepository.deleteFilmsDetailsOlderTimestamp(
            System.currentTimeMillis() - FILM_DETAILS_EXPIRE_PERIOD_MILLISECONDS
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
