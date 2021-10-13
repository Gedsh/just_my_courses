package pan.alexander.githubclient.domain.users

import io.reactivex.rxjava3.core.Single
import pan.alexander.githubclient.domain.entities.GithubUser

interface UsersInteractor {
    fun getUsers(): Single<List<GithubUser>>
}
