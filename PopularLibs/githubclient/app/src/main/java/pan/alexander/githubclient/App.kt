package pan.alexander.githubclient

import android.app.Application
import pan.alexander.githubclient.di.*
import pan.alexander.githubclient.di.app.*

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
            .contextModule(ContextModule(this))
            .ciceroneModule(CiceroneModule())
            .retrofitModule(RetrofitModule())
            .roomModule(RoomModule())
            .build()
    }
}
