package pan.alexander.filmrevealer.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object ConnectionUtils {
    @Suppress("deprecation")
    fun isInternetAvailable(context: Context): Boolean {
        var result = false

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val networkCapabilities =
                connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)

            networkCapabilities?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                ) {
                    result = true
                }
            }
        } else {
            val networkInfo = connectivityManager?.activeNetworkInfo
            networkInfo?.let { result = it.isConnected }
        }

        return result
    }
}
