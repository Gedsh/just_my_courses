package pan.alexander.pictureoftheday.framework

import android.app.Application
import pan.alexander.pictureoftheday.di.*

class App : Application() {
    companion object {
        lateinit var instance: App
        const val LOG_TAG = "pictureoftheday"
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
            .retrofitModule(RetrofitModule())
            .preferencesModule(PreferencesModule(instance))
            .build()
    }
}
