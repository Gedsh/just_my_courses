package pan.alexander.testweatherapp.di

import org.koin.dsl.module
import pan.alexander.testweatherapp.domain.CurrentWeatherInteractor
import pan.alexander.testweatherapp.domain.CurrentWeatherInteractorImpl

object InteractorsModule {
    fun provideCurrentWeatherInteractor() = module {
        factory<CurrentWeatherInteractor> {
            CurrentWeatherInteractorImpl(
                localRepository = get(),
                remoteRepository = get()
            )
        }
    }
}
