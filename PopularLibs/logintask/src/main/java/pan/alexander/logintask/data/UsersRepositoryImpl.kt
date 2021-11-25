package pan.alexander.logintask.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import pan.alexander.logintask.di.DISPATCHER_IO
import pan.alexander.logintask.domain.UsersRepository
import pan.alexander.logintask.domain.entities.User
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private const val NETWORK_RESPONSE_DELAY = 3000L

@Singleton
class UsersRepositoryImpl @Inject constructor(
    @Named(DISPATCHER_IO)
    private val dispatcherIo: CoroutineDispatcher
) : UsersRepository {

    private val users = mutableSetOf<User>()

    override suspend fun getUser(login: String): User? {
        return withContext(dispatcherIo) {
            delay(NETWORK_RESPONSE_DELAY)
            users.firstOrNull { it.login == login }
        }
    }

    override suspend fun saveUserData(user: User) {
        withContext(dispatcherIo) {
            delay(NETWORK_RESPONSE_DELAY)
            users.add(user)
        }
    }
}
