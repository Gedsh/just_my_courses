package pan.alexander.githubclient.data.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import pan.alexander.githubclient.domain.LocalRepository
import pan.alexander.githubclient.domain.entities.GithubUser
import pan.alexander.githubclient.domain.entities.UserGithubRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : LocalRepository {
    override fun getAllUsers(): Flowable<List<GithubUser>> =
        localDataSource.getAllUsers()

    override fun addUsers(users: List<GithubUser>): Completable =
        localDataSource.addUsers(users)

    override fun getUserRepos(userId: Long): Flowable<List<UserGithubRepo>> =
        localDataSource.getUserRepos(userId)

    override fun addRepos(repos: List<UserGithubRepo>): Completable =
        localDataSource.addRepos(repos)
}
