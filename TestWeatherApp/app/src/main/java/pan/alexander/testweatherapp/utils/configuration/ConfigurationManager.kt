package pan.alexander.testweatherapp.utils.configuration

interface ConfigurationManager {
    fun getApiKey(): String
    fun getBaseUrl(): String
}
