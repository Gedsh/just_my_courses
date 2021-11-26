package pan.alexander.githubclient.di.app

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pan.alexander.githubclient.database.AppDatabase
import pan.alexander.githubclient.database.ListTypeConverter
import pan.alexander.githubclient.database.UserDao
import pan.alexander.githubclient.database.UserReposDao
import pan.alexander.githubclient.utils.configuration.ConfigurationManager
import javax.inject.Singleton

@Module
class RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        context: Context,
        configurationManager: ConfigurationManager,
        listTypeConverter: ListTypeConverter
    ): AppDatabase = Room
        .databaseBuilder(
            context,
            AppDatabase::class.java,
            configurationManager.getAppDatabaseName()
        )
        .addTypeConverter(listTypeConverter)
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao

    @Provides
    @Singleton
    fun provideUserReposDao(appDatabase: AppDatabase): UserReposDao = appDatabase.userReposDao
}
