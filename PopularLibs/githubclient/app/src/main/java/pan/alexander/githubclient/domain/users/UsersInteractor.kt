package pan.alexander.githubclient.domain.users

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import pan.alexander.githubclient.domain.entities.GithubUser

interface UsersInteractor {
    fun getUsers(): Flowable<List<GithubUser>>
    fun updateUsers(): Completable
}
