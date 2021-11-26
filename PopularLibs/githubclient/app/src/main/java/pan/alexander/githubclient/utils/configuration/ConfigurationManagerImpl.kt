package pan.alexander.githubclient.utils.configuration

import pan.alexander.githubclient.BuildConfig
import javax.inject.Inject

class ConfigurationManagerImpl @Inject constructor() : ConfigurationManager {

    override fun getBaseUrl(): String = BuildConfig.API_BASE_URL

    override fun getOwnRepoId() = BuildConfig.OWN_REPO_ID

    override fun getAppDatabaseName(): String = "main_database.db"
}
