package pan.alexander.githubclient.domain

import pan.alexander.githubclient.domain.entities.GithubUser

interface UsersRepository {
    fun getUsers(): List<GithubUser>
}
