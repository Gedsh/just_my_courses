package pan.alexander.githubclient.data

import pan.alexander.githubclient.domain.UsersRepository
import pan.alexander.githubclient.domain.entities.GithubUser
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor() : UsersRepository {

    private val users = listOf(
        GithubUser("login1"),
        GithubUser("login2"),
        GithubUser("login3"),
        GithubUser("login4"),
        GithubUser("login5")
    )

    override fun getUsers(): List<GithubUser> = users
}
