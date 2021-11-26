package pan.alexander.githubclient.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import pan.alexander.githubclient.domain.entities.GithubUser

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): Flowable<List<GithubUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<GithubUser>): Completable
}
