package pan.alexander.filmrevealer.di

import dagger.Binds
import dagger.Module
import pan.alexander.filmrevealer.data.local.LocalRepositoryImpl
import pan.alexander.filmrevealer.data.preferences.PreferenceRepositoryImpl
import pan.alexander.filmrevealer.data.remote.RemoteRepositoryImpl
import pan.alexander.filmrevealer.domain.local.LocalRepository
import pan.alexander.filmrevealer.domain.preferences.PreferencesRepository
import pan.alexander.filmrevealer.domain.remote.RemoteRepository

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideLocalRepository(localRepository: LocalRepositoryImpl): LocalRepository

    @Binds
    abstract fun provideRemoteRepository(remoteRepository: RemoteRepositoryImpl): RemoteRepository

    @Binds
    abstract fun providePreferencesRepository(preferencesRepository: PreferenceRepositoryImpl): PreferencesRepository
}
