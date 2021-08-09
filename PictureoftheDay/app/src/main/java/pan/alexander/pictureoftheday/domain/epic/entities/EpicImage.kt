package pan.alexander.pictureoftheday.domain.epic.entities

import androidx.annotation.Keep

@Keep
data class EpicImage(
    val url: String?,
    val description: String?
)
