package pan.alexander.githubclient.domain

interface NetworkRepository {
    fun isInternetAvailable(): Boolean
}
