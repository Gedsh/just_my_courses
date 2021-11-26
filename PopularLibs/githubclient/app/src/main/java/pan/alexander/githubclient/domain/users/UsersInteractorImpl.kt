package pan.alexander.githubclient.domain.users

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import pan.alexander.githubclient.di.ActivityScope
import pan.alexander.githubclient.domain.LocalRepository
import pan.alexander.githubclient.domain.RemoteRepository
import pan.alexander.githubclient.domain.entities.GithubUser
import javax.inject.Inject

@ActivityScope
class UsersInteractorImpl @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : UsersInteractor {

    override fun getUsers(): Flowable<List<GithubUser>> =
        localRepository.getAllUsers()
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun updateUsers(): Completable =
        remoteRepository.loadUsers()
            .flatMapCompletable {
                localRepository.addUsers(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
