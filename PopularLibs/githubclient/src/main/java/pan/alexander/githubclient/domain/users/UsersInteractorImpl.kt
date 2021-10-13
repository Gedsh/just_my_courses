package pan.alexander.githubclient.domain.users

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import pan.alexander.githubclient.domain.UsersRepository
import pan.alexander.githubclient.domain.entities.GithubUser
import javax.inject.Inject

class UsersInteractorImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : UsersInteractor {
    override fun getUsers(): Single<List<GithubUser>> =
        usersRepository.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
