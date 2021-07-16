package pan.alexander.training

import android.app.Application
import pan.alexander.training.di.*

class App : Application() {
    companion object {
        lateinit var instance: App
        const val LOG_TAG = "training"
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
            .contextModule(ContextModule(instance))
            .contentResolverModule(ContentResolverModule(instance))
            .locationModule(LocationModule(instance))
            .build()
    }
}
