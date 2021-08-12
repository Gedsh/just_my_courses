package pan.alexander.pictureoftheday.utils.configuration

import pan.alexander.pictureoftheday.BuildConfig
import javax.inject.Inject

class ConfigurationManagerImpl @Inject constructor() : ConfigurationManager {
    override fun getApiKey() = BuildConfig.API_KEY

    override fun getBaseUrl() = BuildConfig.API_BASE_URL
}
