package pan.alexander.githubclient

import android.app.Application
import pan.alexander.githubclient.di.ApplicationComponent
import pan.alexander.githubclient.di.CiceroneModule
import pan.alexander.githubclient.di.DaggerApplicationComponent

class App : Application() {
    companion object {
        const val LOG_TAG = "githubclient"
    }

    lateinit var daggerComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        initDaggerComponent()
    }

    private fun initDaggerComponent() {
        daggerComponent = DaggerApplicationComponent
            .builder()
            .ciceroneModule(CiceroneModule())
            .build()
    }
}
