package pan.alexander.fileconverter

import android.app.Application
import pan.alexander.fileconverter.di.ApplicationComponent
import pan.alexander.fileconverter.di.ContextModule
import pan.alexander.fileconverter.di.DaggerApplicationComponent

class App : Application() {
    companion object {
        const val LOG_TAG = "fileconverter"
    }

    lateinit var daggerComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        initDaggerComponent()
    }

    private fun initDaggerComponent() {
        daggerComponent = DaggerApplicationComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }
}
