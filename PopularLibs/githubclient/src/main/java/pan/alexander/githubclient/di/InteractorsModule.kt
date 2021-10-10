package pan.alexander.githubclient.di

import dagger.Binds
import dagger.Module
import pan.alexander.githubclient.domain.users.UsersInteractor
import pan.alexander.githubclient.domain.users.UsersInteractorImpl

@Module
abstract class InteractorsModule {

    @Binds
    abstract fun provideUsersInteractor(usersInteractor: UsersInteractorImpl): UsersInteractor
}
