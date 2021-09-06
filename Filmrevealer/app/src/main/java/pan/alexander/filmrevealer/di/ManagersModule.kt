package pan.alexander.filmrevealer.di

import dagger.Binds
import dagger.Module
import pan.alexander.filmrevealer.utils.configuration.ConfigurationManager
import pan.alexander.filmrevealer.utils.configuration.ConfigurationManagerImpl
import pan.alexander.filmrevealer.utils.locales.LocaleManager
import pan.alexander.filmrevealer.utils.locales.LocaleManagerImpl

@Module
abstract class ManagersModule {

    @Binds
    abstract fun provideConfigurationManager(
        configurationManager: ConfigurationManagerImpl
    ): ConfigurationManager

    @Binds
    abstract fun provideLocaleManager(localeManager: LocaleManagerImpl): LocaleManager
}
