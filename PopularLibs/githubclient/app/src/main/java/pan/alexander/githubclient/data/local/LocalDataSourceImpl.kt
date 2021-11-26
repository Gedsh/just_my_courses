package pan.alexander.githubclient.data.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import pan.alexander.githubclient.database.UserDao
import pan.alexander.githubclient.database.UserReposDao
import pan.alexander.githubclient.domain.entities.GithubUser
import pan.alexander.githubclient.domain.entities.UserGithubRepo
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    private val userReposDao: UserReposDao
) : LocalDataSource {
    override fun getAllUsers(): Flowable<List<GithubUser>> = userDao.getAll()

    override fun addRepos(repos: List<UserGithubRepo>): Completable = userReposDao.insert(repos)

    override fun getUserRepos(userId: Long): Flowable<List<UserGithubRepo>> =
        userReposDao.getForUserId(userId)

    override fun addUsers(users: List<GithubUser>): Completable = userDao.insert(users)

}
