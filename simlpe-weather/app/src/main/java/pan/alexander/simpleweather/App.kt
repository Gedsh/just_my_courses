package pan.alexander.simpleweather

import android.app.Application
import pan.alexander.simpleweather.di.*

class App : Application() {

    companion object{
        lateinit var instance: App
        const val BASE_URL = "https://api.openweathermap.org/"
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