package pan.alexander.githubclient.domain.repos

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import pan.alexander.githubclient.di.ActivityScope
import pan.alexander.githubclient.domain.LocalRepository
import pan.alexander.githubclient.domain.RemoteRepository
import pan.alexander.githubclient.domain.entities.UserGithubRepo
import javax.inject.Inject

@ActivityScope
class UserReposInteractorImpl @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : UserReposInteractor {

    override fun getUserRepos(userId: Long): Flowable<List<UserGithubRepo>> =
        localRepository.getUserRepos(userId)
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun updateUserRepos(url: String): Completable =
        remoteRepository.loadUserRepos(url)
            .flatMapCompletable {
                if (it.isNotEmpty()) {
                    localRepository.addRepos(it)
                } else {
                    Completable.error(RuntimeException("User has no repos"))
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
