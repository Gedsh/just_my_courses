package pan.alexander.githubclient.data.remote

import io.reactivex.rxjava3.core.Single
import pan.alexander.githubclient.domain.RemoteRepository
import pan.alexander.githubclient.domain.entities.GithubUser
import pan.alexander.githubclient.domain.entities.UserGithubRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : RemoteRepository {

    override fun loadUsers(): Single<List<GithubUser>> =
        remoteDataSource.loadGithubUsers()
            .map { users ->
                users.map { GithubUserMapper.map(it) }
                    .filter { it.id != 0L }
            }

    override fun loadUserRepos(url: String): Single<List<UserGithubRepo>> =
        remoteDataSource.loadUserGithubRepos(url)
            .map { repos ->
                repos.map { UserGithubRepoMapper.map(it) }
                    .filter { it.id != 0L }
            }

}
