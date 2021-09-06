package pan.alexander.filmrevealer.utils.locales

import android.os.Build
import pan.alexander.filmrevealer.App
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocaleManagerImpl @Inject constructor(): LocaleManager {
    override fun getLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            App.instance.resources.configuration.locales[0]
        } else {
            App.instance.resources.configuration.locale
        }
    }

    override fun getLanguage(): String {
        val locale = getLocale()
        return "${locale.language}-${locale.country}"
    }

    override fun getRegion(): String {
        val locale = getLocale()
        return locale.country.uppercase()
    }
}
