package pan.alexander.logintask.domain

import pan.alexander.logintask.domain.entities.User

interface UsersRepository {
    suspend fun getUser(login: String): User?
    suspend fun saveUserData(user: User)
}
