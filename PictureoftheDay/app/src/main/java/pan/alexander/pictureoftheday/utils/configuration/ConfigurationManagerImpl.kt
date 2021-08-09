package pan.alexander.pictureoftheday.utils.configuration

import pan.alexander.pictureoftheday.BuildConfig
import javax.inject.Inject

class ConfigurationManagerImpl @Inject constructor() : ConfigurationManager {
    override fun getApiKey() = BuildConfig.API_KEY

    override fun getNasaBaseUrl() = BuildConfig.API_NASA_BASE_URL

    override fun getEpicBaseUrl() = BuildConfig.API_EPIC_BASE_URL
}
