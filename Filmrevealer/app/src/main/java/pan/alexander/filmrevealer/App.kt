package pan.alexander.filmrevealer

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import pan.alexander.filmrevealer.di.*

class App : Application() {

    init {
        System.setProperty("rx3.purge-enabled", "false")
        System.setProperty("rx2.purge-enabled", "false")
        System.setProperty("rx3.computation-threads", "1")
        System.setProperty("rx2.computation-threads", "1")
    }

    companion object {
        lateinit var instance: App
        const val LOG_TAG = "filmrevealer"
    }

    lateinit var daggerComponent: ApplicationComponent

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (BuildConfig.DEBUG) {
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
            .dataStoreModule(DataStoreModule(instance))
            .build()
    }
}
