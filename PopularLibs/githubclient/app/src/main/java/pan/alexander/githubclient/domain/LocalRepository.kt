package pan.alexander.githubclient.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import pan.alexander.githubclient.domain.entities.GithubUser
import pan.alexander.githubclient.domain.entities.UserGithubRepo

interface LocalRepository {
    fun getAllUsers(): Flowable<List<GithubUser>>
    fun addUsers(users: List<GithubUser>): Completable

    fun getUserRepos(userId: Long): Flowable<List<UserGithubRepo>>
    fun addRepos(repos: List<UserGithubRepo>): Completable
}
