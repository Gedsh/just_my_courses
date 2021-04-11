package pan.alexander.simpleweather.di

import dagger.Module
import dagger.Provides
import pan.alexander.simpleweather.data.CurrentWeatherRepositoryImpl
import pan.alexander.simpleweather.domain.CurrentWeatherRepository

@Module
class RepositoryModule {
    @Provides
    fun providesCurrentWeatherRepository(): CurrentWeatherRepository {
        return CurrentWeatherRepositoryImpl()
    }
}
