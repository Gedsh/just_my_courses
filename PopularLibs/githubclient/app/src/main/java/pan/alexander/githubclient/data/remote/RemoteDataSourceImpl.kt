package pan.alexander.githubclient.data.remote

import io.reactivex.rxjava3.core.Single
import pan.alexander.githubclient.data.remote.pojo.GithubUserPojo
import pan.alexander.githubclient.data.remote.pojo.UserGithubRepoPojo
import pan.alexander.githubclient.utils.configuration.ConfigurationManager
import pan.alexander.githubclient.web.GithubUserApi
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val githubUserApi: GithubUserApi,
    private val configurationManager: ConfigurationManager
) : RemoteDataSource {
    override fun loadGithubUsers(): Single<List<GithubUserPojo>> =
        githubUserApi.loadUsers(configurationManager.getOwnRepoId() - 1)

    override fun loadUserGithubRepos(url: String): Single<List<UserGithubRepoPojo>> =
        githubUserApi.loadUserRepos(url)
}
