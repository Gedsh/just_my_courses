package pan.alexander.githubclient.domain.users

import pan.alexander.githubclient.domain.UsersRepository
import pan.alexander.githubclient.domain.entities.GithubUser
import javax.inject.Inject

class UsersInteractorImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : UsersInteractor {
    override fun getUsers(): List<GithubUser> = usersRepository.getUsers()
}
