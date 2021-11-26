package pan.alexander.githubclient.di.main

import dagger.Binds
import dagger.Module
import pan.alexander.githubclient.domain.repos.UserReposInteractor
import pan.alexander.githubclient.domain.repos.UserReposInteractorImpl
import pan.alexander.githubclient.domain.users.UsersInteractor
import pan.alexander.githubclient.domain.users.UsersInteractorImpl

@Module
abstract class MainInteractorsModule {

    @Binds
    abstract fun provideUsersInteractor(usersInteractor: UsersInteractorImpl): UsersInteractor

    @Binds
    abstract fun provideReposInteractor(reposInteractor: UserReposInteractorImpl): UserReposInteractor
}
