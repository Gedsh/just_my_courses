package pan.alexander.logintask.di

import dagger.Binds
import dagger.Module
import pan.alexander.logintask.ui.login.Contract
import pan.alexander.logintask.ui.login.LoginPresenter

@Module
abstract class PresentersModule {

    @Binds
    abstract fun provideLoginPresenter(loginPresenter: LoginPresenter): Contract.Presenter
}
