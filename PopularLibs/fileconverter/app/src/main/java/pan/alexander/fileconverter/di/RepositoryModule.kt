package pan.alexander.fileconverter.di

import dagger.Binds
import dagger.Module
import pan.alexander.fileconverter.data.MainRepositoryImpl
import pan.alexander.fileconverter.domain.MainRepository

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideMainRepository(mainRepository: MainRepositoryImpl): MainRepository
}
