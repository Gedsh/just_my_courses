package pan.alexander.githubclient.di.app

import dagger.Binds
import dagger.Module
import pan.alexander.githubclient.data.local.LocalRepositoryImpl
import pan.alexander.githubclient.data.network.NetworkRepositoryImpl
import pan.alexander.githubclient.data.remote.RemoteRepositoryImpl
import pan.alexander.githubclient.domain.LocalRepository
import pan.alexander.githubclient.domain.NetworkRepository
import pan.alexander.githubclient.domain.RemoteRepository

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideLocalRepository(localRepository: LocalRepositoryImpl): LocalRepository

    @Binds
    abstract fun provideRemoteRepository(remoteRepository: RemoteRepositoryImpl): RemoteRepository

    @Binds
    abstract fun provideNetworkRepository(networkRepository: NetworkRepositoryImpl): NetworkRepository
}
