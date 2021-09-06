package pan.alexander.filmrevealer.utils.locales

import java.util.*

interface LocaleManager {
    fun getLocale(): Locale
    fun getLanguage(): String
    fun getRegion(): String
}
