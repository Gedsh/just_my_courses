package pan.alexander.githubclient.di

import dagger.Binds
import dagger.Module
import pan.alexander.githubclient.data.UsersRepositoryImpl
import pan.alexander.githubclient.domain.UsersRepository

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideUsersRepository(
        repository: UsersRepositoryImpl
    ): UsersRepository
}
