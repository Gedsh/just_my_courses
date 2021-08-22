package pan.alexander.testweatherapp

import android.app.Application

class App : Application() {
    companion object {
        lateinit var instance: App
        const val LOG_TAG = "weatherapp"
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}
