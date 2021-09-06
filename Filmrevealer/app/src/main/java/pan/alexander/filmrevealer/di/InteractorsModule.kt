package pan.alexander.filmrevealer.di

import dagger.Binds
import dagger.Module
import pan.alexander.filmrevealer.domain.interactors.MainInteractor
import pan.alexander.filmrevealer.domain.interactors.MainInteractorImpl

@Module
abstract class InteractorsModule {
    @Binds
    abstract fun provideMainInteractor(mainInteractor: MainInteractorImpl): MainInteractor
}
