package pan.alexander.githubclient.di.app

import dagger.Binds
import dagger.Module
import pan.alexander.githubclient.data.local.LocalDataSource
import pan.alexander.githubclient.data.local.LocalDataSourceImpl
import pan.alexander.githubclient.data.remote.RemoteDataSource
import pan.alexander.githubclient.data.remote.RemoteDataSourceImpl

@Module
abstract class DataSourceModule {

    @Binds
    abstract fun provideLocalDataSource(localDataSource: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun provideRemoteDataSource(remoteDataSource: RemoteDataSourceImpl): RemoteDataSource
}
