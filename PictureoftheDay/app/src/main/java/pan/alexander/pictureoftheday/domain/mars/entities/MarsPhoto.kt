package pan.alexander.pictureoftheday.domain.mars.entities

import androidx.annotation.Keep

@Keep
data class MarsPhoto(
    val rover: String,
    val url: String?
)
