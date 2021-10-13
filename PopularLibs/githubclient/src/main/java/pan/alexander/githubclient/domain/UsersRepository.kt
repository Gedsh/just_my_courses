package pan.alexander.githubclient.domain

import io.reactivex.rxjava3.core.Single
import pan.alexander.githubclient.domain.entities.GithubUser

interface UsersRepository {
    fun getUsers(): Single<List<GithubUser>>
}
