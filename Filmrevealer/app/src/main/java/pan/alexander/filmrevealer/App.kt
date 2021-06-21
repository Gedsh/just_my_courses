package pan.alexander.filmrevealer

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import pan.alexander.filmrevealer.di.*

class App : Application() {
    companion object {
        lateinit var instance: App
        const val BASE_URL = "https://api.themoviedb.org/"
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/original/"
        const val LOG_TAG = "filmrevealer"
    }

    lateinit var daggerComponent: ApplicationComponent

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if(BuildConfig.DEBUG) {
            MultiDex.install(this)
        }
    }

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
            .mainThreadHandler(MainThreadHandler(instance))
            .build()
    }
}
