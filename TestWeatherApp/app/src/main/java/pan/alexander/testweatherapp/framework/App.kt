package pan.alexander.testweatherapp.framework

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import pan.alexander.testweatherapp.di.*

class App : Application() {
    companion object {
        lateinit var instance: App
        const val LOG_TAG = "weatherapp"
    }

    @KoinExperimentalAPI
    override fun onCreate() {
        super.onCreate()

        instance = this

        initKoin()
    }

    @KoinExperimentalAPI
    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            fragmentFactory()
            modules(
                listOf(
                    AppModule.provideMainViewModel(),
                    AppModule.provideWeatherFragment(),
                    ConfigurationModule.provideConfigurationManager(),
                    RetrofitModule.provideServiceApi(),
                    RoomModule.provideAppDatabase(),
                    DataSourcesModule.provideLocalDataSource(),
                    DataSourcesModule.provideRemoteDataSource(),
                    RepositoryModule.provideLocalRepository(),
                    RepositoryModule.provideRemoteRepository(),
                    InteractorsModule.provideCurrentWeatherInteractor(),
                    CoroutinesModule.provideDispatcherIO(),
                )
            )
        }
    }
}
