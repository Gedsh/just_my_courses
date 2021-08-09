package pan.alexander.pictureoftheday.domain.pod

import kotlinx.coroutines.flow.Flow
import pan.alexander.pictureoftheday.domain.pod.entities.NasaPicture
import java.util.*

interface PictureInteractor {
    suspend fun getPhotoByDate(date: Date): Flow<NasaPicture>
}
