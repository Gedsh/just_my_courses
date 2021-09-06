package pan.alexander.testweatherapp.di

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pan.alexander.testweatherapp.framework.ui.MainActivity
import pan.alexander.testweatherapp.framework.ui.main.WeatherFragment
import pan.alexander.testweatherapp.framework.ui.main.WeatherViewModel

object AppModule {
    fun provideMainViewModel() = module {
        viewModel { WeatherViewModel(interactor = get()) }
    }

    fun provideWeatherFragment() = module {
        scope<MainActivity> {
            fragment { WeatherFragment() }
        }
    }
}
