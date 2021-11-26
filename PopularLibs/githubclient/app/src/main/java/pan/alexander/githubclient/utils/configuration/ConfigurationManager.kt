package pan.alexander.githubclient.utils.configuration

interface ConfigurationManager {
    fun getBaseUrl(): String
    fun getOwnRepoId(): Long
    fun getAppDatabaseName(): String
}
