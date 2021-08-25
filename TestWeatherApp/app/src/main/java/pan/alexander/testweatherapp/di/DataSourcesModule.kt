package pan.alexander.testweatherapp.di

import org.koin.dsl.module
import pan.alexander.testweatherapp.data.LocalDataSource
import pan.alexander.testweatherapp.data.LocalDataSourceImpl
import pan.alexander.testweatherapp.data.RemoteDataSource
import pan.alexander.testweatherapp.data.RemoteDataSourceImpl

object DataSourcesModule {
    fun provideLocalDataSource() = module {
        single<LocalDataSource> {
            LocalDataSourceImpl(
                currentWeatherDao = get(),
                dispatcherIO = get()
            )
        }
    }

    fun provideRemoteDataSource() = module {
        single<RemoteDataSource> {
            RemoteDataSourceImpl(
                currentWeatherApiService = get(),
                configurationManager = get(),
                dispatcherIO = get()
            )
        }
    }
}
