package pan.alexander.testweatherapp.utils.configuration

import android.util.Base64
import pan.alexander.testweatherapp.BuildConfig

class ConfigurationManagerImpl : ConfigurationManager {
    override fun getApiKey() = Base64.decode(BuildConfig.API_KEY, Base64.DEFAULT)
        .toString(charset("UTF-8"))

    override fun getBaseUrl() = BuildConfig.API_OWM_BASE_URL
}
