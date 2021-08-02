package pan.alexander.pictureoftheday.di

import dagger.Binds
import dagger.Module
import pan.alexander.pictureoftheday.utils.configuration.ConfigurationManager
import pan.alexander.pictureoftheday.utils.configuration.ConfigurationManagerImpl

@Module
abstract class ConfigurationModule {
    @Binds
    abstract fun provideConfigurationManager(
        configurationManager: ConfigurationManagerImpl
    ): ConfigurationManager
}
