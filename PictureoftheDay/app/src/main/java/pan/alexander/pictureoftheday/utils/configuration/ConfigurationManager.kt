package pan.alexander.pictureoftheday.utils.configuration

interface ConfigurationManager {
    fun getApiKey(): String
    fun getNasaBaseUrl(): String
    fun getEpicBaseUrl(): String
}
