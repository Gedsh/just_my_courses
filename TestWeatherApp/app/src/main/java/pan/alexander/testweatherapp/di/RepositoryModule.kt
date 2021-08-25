package pan.alexander.testweatherapp.di

import org.koin.dsl.module
import pan.alexander.testweatherapp.data.LocalRepositoryImpl
import pan.alexander.testweatherapp.data.RemoteRepositoryImpl
import pan.alexander.testweatherapp.data.ServerResponseToCurrentWeatherMapper
import pan.alexander.testweatherapp.domain.LocalRepository
import pan.alexander.testweatherapp.domain.RemoteRepository

object RepositoryModule {
    fun provideLocalRepository() = module {
        single<LocalRepository> {
            LocalRepositoryImpl(
                localDataSource = get()
            )
        }
    }

    fun provideRemoteRepository() = module {
        single<RemoteRepository> {
            RemoteRepositoryImpl(
                remoteDataSource = get(),
                mapper = ServerResponseToCurrentWeatherMapper()
            )
        }
    }
}
