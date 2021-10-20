package pan.alexander.fileconverter.di

import dagger.Binds
import dagger.Module
import pan.alexander.fileconverter.domain.MainInteractor
import pan.alexander.fileconverter.domain.MainInteractorImpl

@Module
abstract class InteractorsModule {

    @Binds
    abstract fun provideMainInteractor(mainInteractor: MainInteractorImpl): MainInteractor
}
