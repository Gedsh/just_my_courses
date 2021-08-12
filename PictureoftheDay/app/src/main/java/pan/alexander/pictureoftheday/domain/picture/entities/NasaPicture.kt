package pan.alexander.pictureoftheday.domain.picture.entities

import androidx.annotation.Keep
import java.util.*

@Keep
data class NasaPicture(
    val title: String,
    val date: Date?,
    val explanation: String,
    val url: String?
)
