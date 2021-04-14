package pan.alexander.simpleweather.di

import android.app.Application
import dagger.Component
import pan.alexander.simpleweather.data.CurrentWeatherRepositoryImpl
import pan.alexander.simpleweather.data.database.AppDatabase
import pan.alexander.simpleweather.data.database.CurrentWeatherDao
import pan.alexander.simpleweather.data.web.CurrentWeatherApiService
import pan.alexander.simpleweather.domain.MainInteractor
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, AppContextModule::class, RoomModule::class, RetrofitModule::class])
interface ApplicationComponent {
    fun getInteractor(): MainInteractor

    fun getRepository(): CurrentWeatherRepositoryImpl

    fun getAppContext(): Application

    fun getAppDatabase(): AppDatabase
    fun getCurrentWeatherDao(): CurrentWeatherDao

    fun getCurrentWeatherApiService(): CurrentWeatherApiService
}
