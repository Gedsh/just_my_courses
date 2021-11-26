package pan.alexander.githubclient.di.main

import dagger.Binds
import dagger.Module
import pan.alexander.githubclient.ui.users.adapter.AdapterContract
import pan.alexander.githubclient.ui.users.adapter.UsersListPresenter

@Module
abstract class MainPresentersModule {

    @Binds
    abstract fun provideUsersListPresenter(presenter: UsersListPresenter): AdapterContract.UserListPresenter
}
