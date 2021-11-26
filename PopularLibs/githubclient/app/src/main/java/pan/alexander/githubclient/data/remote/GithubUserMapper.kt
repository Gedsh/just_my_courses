package pan.alexander.githubclient.data.remote

import pan.alexander.githubclient.data.remote.pojo.GithubUserPojo
import pan.alexander.githubclient.domain.entities.GithubUser

object GithubUserMapper {
    fun map(githubUserPojo: GithubUserPojo): GithubUser =
        GithubUser(
            id = githubUserPojo.id ?: 0,
            login = githubUserPojo.login ?: "",
            avatarURL = githubUserPojo.avatarURL ?: "",
            reposURL = githubUserPojo.reposURL ?: ""
        )
}
