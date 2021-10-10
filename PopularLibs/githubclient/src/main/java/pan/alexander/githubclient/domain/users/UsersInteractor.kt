package pan.alexander.githubclient.domain.users

import pan.alexander.githubclient.domain.entities.GithubUser

interface UsersInteractor {
    fun getUsers(): List<GithubUser>
}
