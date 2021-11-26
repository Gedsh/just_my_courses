package pan.alexander.githubclient.data.remote

import io.reactivex.rxjava3.core.Single
import pan.alexander.githubclient.data.remote.pojo.GithubUserPojo
import pan.alexander.githubclient.data.remote.pojo.UserGithubRepoPojo

interface RemoteDataSource {
    fun loadGithubUsers(): Single<List<GithubUserPojo>>
    fun loadUserGithubRepos(url: String): Single<List<UserGithubRepoPojo>>
}
