package pan.alexander.filmrevealer

import android.app.Application
import pan.alexander.filmrevealer.di.ApplicationComponent
import pan.alexander.filmrevealer.di.DaggerApplicationComponent
import pan.alexander.filmrevealer.di.RetrofitModule
import pan.alexander.filmrevealer.di.RoomModule

class App : Application() {
    companion object {
        lateinit var instance: App
        const val BASE_URL = "https://api.themoviedb.org/"
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/original/"
        const val LOG_TAG = "filmrevealer"
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
            .retrofitModule(RetrofitModule())
            .roomModule(RoomModule(instance))
            .build()
    }
}
