package pan.alexander.githubclient.domain

import io.reactivex.rxjava3.core.Single
import pan.alexander.githubclient.domain.entities.GithubUser
import pan.alexander.githubclient.domain.entities.UserGithubRepo

interface RemoteRepository {
    fun loadUsers(): Single<List<GithubUser>>
    fun loadUserRepos(url: String): Single<List<UserGithubRepo>>
}
