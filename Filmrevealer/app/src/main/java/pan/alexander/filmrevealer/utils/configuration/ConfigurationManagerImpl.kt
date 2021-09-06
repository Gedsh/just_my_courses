package pan.alexander.filmrevealer.utils.configuration

import android.util.Base64
import pan.alexander.filmrevealer.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigurationManagerImpl @Inject constructor(): ConfigurationManager {
    override fun getApiKey(): String {
        return Base64.decode("${BuildConfig.API_KEY}=", Base64.DEFAULT)
            .toString(charset("UTF-8"))
    }

    override fun getBaseUrl(): String = BuildConfig.API_BASE_URL
}
