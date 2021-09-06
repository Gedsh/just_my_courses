package pan.alexander.filmrevealer.di

import dagger.Binds
import dagger.Module
import pan.alexander.filmrevealer.data.local.LocalDataSource
import pan.alexander.filmrevealer.data.local.LocalDataSourceImpl
import pan.alexander.filmrevealer.data.remote.RemoteDataSource
import pan.alexander.filmrevealer.data.remote.RemoteDataSourceImpl

@Module
abstract class DataSourcesModule {

    @Binds
    abstract fun provideLocalDataSource(localDateSource: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun provideRemoteDataSource(remoteDataSource: RemoteDataSourceImpl): RemoteDataSource
}
