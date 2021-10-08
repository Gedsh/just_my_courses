package pan.alexander.logintask.ui

import android.app.Application
import pan.alexander.logintask.di.ApplicationComponent
import pan.alexander.logintask.di.CoroutinesModule
import pan.alexander.logintask.di.DaggerApplicationComponent

class App : Application() {
    companion object {
        lateinit var instance: App
        const val LOG_TAG = "logintask"
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
            .coroutinesModule(CoroutinesModule())
            .build()
    }
}
