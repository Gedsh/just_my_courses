package pan.alexander.githubclient.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import pan.alexander.githubclient.domain.entities.UserGithubRepo

@Dao
interface UserReposDao {

    @Query("SELECT * FROM repositories WHERE user_id = :userId")
    fun getForUserId(userId: Long): Flowable<List<UserGithubRepo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repos: List<UserGithubRepo>): Completable
}
