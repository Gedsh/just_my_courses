package pan.alexander.pictureoftheday.data.picture

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.jvm.Throws

private const val NASA_POD_DATE_FORMAT = "yyyy-MM-dd"

class DateStringMapper @Inject constructor() {
    fun map(date: Date): String {
        return SimpleDateFormat(NASA_POD_DATE_FORMAT, Locale.US).format(date)
    }

    @Throws(ParseException::class)
    fun map(dateStr: String): Date? {
        return SimpleDateFormat(NASA_POD_DATE_FORMAT, Locale.US).parse(dateStr)
    }
}
