package pan.alexander.githubclient.data.network

import android.content.Context
import pan.alexander.githubclient.domain.NetworkRepository
import pan.alexander.githubclient.utils.NetworkUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImpl @Inject constructor(
    private val context: Context
) : NetworkRepository {

    override fun isInternetAvailable(): Boolean = NetworkUtils.isInternetAvailable(context)
}
