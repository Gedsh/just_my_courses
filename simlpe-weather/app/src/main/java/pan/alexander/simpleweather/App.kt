package pan.alexander.simpleweather

import android.app.Application
import pan.alexander.simpleweather.di.AppContextModule
import pan.alexander.simpleweather.di.MainComponent
import pan.alexander.simpleweather.di.RetrofitModule
import pan.alexander.simpleweather.di.RoomModule

class App : Application() {

    companion object{
        lateinit var instance: App
        const val BASE_URL = "https://api.openweathermap.org/"
    }

    lateinit var daggerComponent: MainComponent

    override fun onCreate() {
        super.onCreate()

        instance = this

        initDaggerComponent()
    }

    private fun initDaggerComponent() {
        daggerComponent = DaggerMainComponent
            .builder()
            .appContextModule(AppContextModule(instance))
            .roomModule(RoomModule(instance))
            .retrofitModule(RetrofitModule())
            .build()
    }
}