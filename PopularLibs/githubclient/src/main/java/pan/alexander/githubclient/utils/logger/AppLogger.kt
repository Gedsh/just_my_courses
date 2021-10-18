package pan.alexander.githubclient.utils.logger

import android.util.Log
import pan.alexander.githubclient.App.Companion.LOG_TAG

object AppLogger {
    fun logI(message: String) = Log.i(LOG_TAG, message)

    fun logW(message: String) = Log.w(LOG_TAG, message)

    fun logE(message: String, throwable: Throwable?) = Log.e(LOG_TAG, message, throwable)
}
