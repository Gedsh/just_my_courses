package pan.alexander.logintask.di

import dagger.Binds
import dagger.Module
import pan.alexander.logintask.data.UsersRepositoryImpl
import pan.alexander.logintask.domain.UsersRepository

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideUsersRepository(usersRepository: UsersRepositoryImpl): UsersRepository
}
