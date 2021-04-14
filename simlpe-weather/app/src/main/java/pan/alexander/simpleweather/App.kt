package pan.alexander.simpleweather

import android.app.Application
import pan.alexander.simpleweather.di.*

class App : Application() {

    companion object{
        lateinit var instance: App
        const val BASE_URL = "https://api.openweathermap.org/"
        const val BASE_IMAGE_URL = "http://openweathermap.org/"
        const val LOG_TAG = "SimpleWeather"
    }

    lateinit var daggerComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        instance = this

        initDaggerComponent()
    }

    private fun initDaggerComponent() {
        daggerComponent = DaggerApplicationComponent
            .builder()
            .appContextModule(AppContextModule(instance))
            .roomModule(RoomModule(instance))
            .retrofitModule(RetrofitModule())
            .build()
    }
}