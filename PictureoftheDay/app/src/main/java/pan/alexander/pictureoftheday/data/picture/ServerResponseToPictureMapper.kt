package pan.alexander.pictureoftheday.data.picture

import android.util.Log
import pan.alexander.pictureoftheday.data.picture.model.ApiServerResponse
import pan.alexander.pictureoftheday.domain.picture.entities.NasaPicture
import pan.alexander.pictureoftheday.framework.App.Companion.LOG_TAG
import java.text.ParseException
import java.util.*
import javax.inject.Inject

class ServerResponseToPictureMapper @Inject constructor(
    private val dateStringMapper: DateStringMapper
) {
    fun map(serverResponse: ApiServerResponse): NasaPicture {
        val title = serverResponse.title ?: ""
        var date: Date? = null
        serverResponse.dateString?.let {
            try {
                date = dateStringMapper.map(it)
            } catch (e: ParseException) {
                Log.e(LOG_TAG, "Error while parsing server response date", e)
            }
        }
        val explanation = serverResponse.explanation ?: ""
        val url = serverResponse.url

        return NasaPicture(
            title = title,
            date = date,
            explanation = explanation,
            url = url
        )
    }
}
