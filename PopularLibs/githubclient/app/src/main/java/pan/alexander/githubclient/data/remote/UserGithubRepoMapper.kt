package pan.alexander.githubclient.data.remote

import pan.alexander.githubclient.data.remote.pojo.UserGithubRepoPojo
import pan.alexander.githubclient.domain.entities.UserGithubRepo

object UserGithubRepoMapper {
    fun map(userRepoPojo: UserGithubRepoPojo): UserGithubRepo =
        UserGithubRepo(
            id = userRepoPojo.id ?: 0,
            name = userRepoPojo.name ?: "",
            description = userRepoPojo.description ?: "",
            createdAt = userRepoPojo.createdAt
                ?.replace("T", " ")
                ?.replace("Z", "") ?: "",
            updatedAt = userRepoPojo.updatedAt
                ?.replace("T", " ")
                ?.replace("Z", "") ?: "",
            forksCount = userRepoPojo.forksCount ?: 0,
            openIssuesCount = userRepoPojo.openIssuesCount ?: 0,
            topics = userRepoPojo.topics ?: emptyList(),
            watchers = userRepoPojo.watchers ?: 0,
            userId = userRepoPojo.owner?.id ?: 0
        )
}
