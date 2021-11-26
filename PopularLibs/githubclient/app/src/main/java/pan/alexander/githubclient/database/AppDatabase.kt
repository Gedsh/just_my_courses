package pan.alexander.githubclient.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pan.alexander.githubclient.domain.entities.GithubUser
import pan.alexander.githubclient.domain.entities.UserGithubRepo

@Database(entities = [GithubUser::class, UserGithubRepo::class], version = 1)
@TypeConverters(ListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val userReposDao: UserReposDao
}
