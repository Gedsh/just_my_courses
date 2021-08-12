package pan.alexander.pictureoftheday.domain.picture

import kotlinx.coroutines.flow.Flow
import pan.alexander.pictureoftheday.domain.picture.entities.NasaPicture
import java.util.*

interface PictureInteractor {
    suspend fun getPhotoByDate(date: Date): Flow<NasaPicture>
}
