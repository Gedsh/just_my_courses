package pan.alexander.githubclient.web

import io.reactivex.rxjava3.core.Single
import pan.alexander.githubclient.data.remote.pojo.GithubUserPojo
import pan.alexander.githubclient.data.remote.pojo.UserGithubRepoPojo
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GithubUserApi {

    @GET("users")
    fun loadUsers(
        @Query("since") since: Long
    ): Single<List<GithubUserPojo>>

    @GET
    fun loadUserRepos(
        @Url url: String
    ): Single<List<UserGithubRepoPojo>>
}
