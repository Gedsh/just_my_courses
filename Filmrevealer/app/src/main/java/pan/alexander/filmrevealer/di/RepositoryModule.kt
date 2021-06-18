package pan.alexander.filmrevealer.di

import dagger.Module
import dagger.Provides
import pan.alexander.filmrevealer.data.LocalRepositoryImplementation
import pan.alexander.filmrevealer.data.RemoteRepositoryImplementation
import pan.alexander.filmrevealer.domain.LocalRepository
import pan.alexander.filmrevealer.domain.RemoteRepository

@Module
class RepositoryModule {

    @Provides
    fun provideLocalRepository(): LocalRepository = LocalRepositoryImplementation()

    @Provides
    fun provideRemoteRepository(): RemoteRepository = RemoteRepositoryImplementation()
}
