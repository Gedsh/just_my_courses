package pan.alexander.githubclient.domain.repos

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import pan.alexander.githubclient.domain.entities.UserGithubRepo

interface UserReposInteractor {
    fun getUserRepos(userId: Long): Flowable<List<UserGithubRepo>>
    fun updateUserRepos(url: String): Completable
}
