package pan.alexander.pictureoftheday.data.epic

import pan.alexander.pictureoftheday.data.DateStringMapper
import pan.alexander.pictureoftheday.data.epic.model.EpicServerResponse
import pan.alexander.pictureoftheday.domain.epic.entities.EpicImage
import pan.alexander.pictureoftheday.utils.configuration.ConfigurationManager
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val NASA_EPIC_PATH_DATE_FORMAT = "yyyy/MM/dd"

class ServerResponseToEpicImageMapper @Inject constructor(
    private val configurationManager: ConfigurationManager,
    private val dateStringMapper: DateStringMapper
) {
    fun map(serverResponse: EpicServerResponse): EpicImage {
        val image = serverResponse.image
        val date = serverResponse.date?.let {
            dateStringMapper.map(it)
        } ?: Date()
        val formattedDateStr = SimpleDateFormat(NASA_EPIC_PATH_DATE_FORMAT, Locale.US).format(date)
        val caption = serverResponse.caption

        return EpicImage(
            url = "${configurationManager.getEpicBaseUrl()}archive/enhanced/" +
                    "$formattedDateStr/jpg/$image.jpg",
            description = caption ?: ""
        )
    }
}
