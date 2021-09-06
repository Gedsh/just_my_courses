package pan.alexander.testweatherapp.di

import org.koin.dsl.module
import pan.alexander.testweatherapp.utils.configuration.ConfigurationManager
import pan.alexander.testweatherapp.utils.configuration.ConfigurationManagerImpl

object ConfigurationModule {
    fun provideConfigurationManager() = module {
        single<ConfigurationManager> { ConfigurationManagerImpl() }
    }
}
