package pan.alexander.training

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import pan.alexander.training.di.*
import pan.alexander.training.services.TrainingFirebaseMessagingService.Companion.FIREBASE_CHANNEL_ID

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createFirebaseNotificationChannel()
        }
    }

    private fun initDaggerComponent() {
        daggerComponent = DaggerApplicationComponent
            .builder()
            .contextModule(ContextModule(instance))
            .contentResolverModule(ContentResolverModule(instance))
            .locationModule(LocationModule(instance))
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createFirebaseNotificationChannel() {
        val notificationManager =
            ContextCompat.getSystemService(this, NotificationManager::class.java)
        val name = getString(R.string.firebase_channel_name)
        val descriptionText = getString(R.string.firebase_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(FIREBASE_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager?.createNotificationChannel(channel)
    }
}
