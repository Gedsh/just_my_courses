package pan.alexander.githubclient.data

import io.reactivex.rxjava3.core.Single
import pan.alexander.githubclient.domain.UsersRepository
import pan.alexander.githubclient.domain.entities.GithubUser
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TEST_NETWORK_DELAY_SEC = 1L

class UsersRepositoryImpl @Inject constructor() : UsersRepository {

    private val users by lazy {
        listOf(
            GithubUser("login1"),
            GithubUser("login2"),
            GithubUser("login3"),
            GithubUser("login4"),
            GithubUser("login5")
        )
    }

    override fun getUsers(): Single<List<GithubUser>> = Single.fromCallable {
        TimeUnit.SECONDS.sleep(TEST_NETWORK_DELAY_SEC)
        users
    }
}
