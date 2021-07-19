package pan.alexander.training.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pan.alexander.training.App.Companion.LOG_TAG
import pan.alexander.training.R
import pan.alexander.training.presentation.activities.MainActivity
import pan.alexander.training.presentation.fragments.NotificationsFragment

class TrainingFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        //To send to the server
        Log.i(LOG_TAG, "Firebase cloud messaging token $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.data.takeIf { it.isNotEmpty() }?.let { handleDataMessage(it.toMap()) }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]
        if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
            showNotification(title, message)
            sendBroadcast(title, message)
        }
    }

    private fun showNotification(title: String, message: String) {
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, FIREBASE_CHANNEL_ID).apply {
                setContentIntent(contentIntent)
                setAutoCancel(true)
                setSmallIcon(R.drawable.ic_notifications_black_24dp)
                setContentTitle(title)
                setContentText(message)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }
        val notificationManager =
            ContextCompat.getSystemService(this, NotificationManager::class.java)
        notificationManager?.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun sendBroadcast(title: String, message: String) {
        val intent = Intent(NotificationsFragment.FIREBASE_ACTION).apply {
            putExtra(NotificationsFragment.EXTRA_PARAM_TITLE, title)
            putExtra(NotificationsFragment.EXTRA_PARAM_MESSAGE, message)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        const val FIREBASE_CHANNEL_ID = "FIREBASE_CHANNEL_ID"
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val NOTIFICATION_ID = 1020
    }
}
