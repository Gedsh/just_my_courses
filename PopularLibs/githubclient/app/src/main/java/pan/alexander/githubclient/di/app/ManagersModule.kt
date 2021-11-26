package pan.alexander.githubclient.di.app

import dagger.Binds
import dagger.Module
import pan.alexander.githubclient.utils.configuration.ConfigurationManager
import pan.alexander.githubclient.utils.configuration.ConfigurationManagerImpl

@Module
abstract class ManagersModule {
    @Binds
    abstract fun provideConfigurationManager(
        configurationManager: ConfigurationManagerImpl
    ): ConfigurationManager
}
