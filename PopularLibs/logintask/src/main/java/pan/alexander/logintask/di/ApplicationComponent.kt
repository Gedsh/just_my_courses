package pan.alexander.logintask.di

import dagger.Component
import pan.alexander.logintask.ui.login.LoginActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, CoroutinesModule::class, PresentersModule::class])
interface ApplicationComponent {
    fun inject(loginActivity: LoginActivity)
}
