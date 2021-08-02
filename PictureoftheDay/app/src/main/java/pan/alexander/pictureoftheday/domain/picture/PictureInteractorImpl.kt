package pan.alexander.pictureoftheday.domain.picture

import kotlinx.coroutines.flow.Flow
import pan.alexander.pictureoftheday.domain.picture.entities.NasaPicture
import java.util.*
import javax.inject.Inject

class PictureInteractorImpl @Inject constructor(
    private val pictureRepository: PictureRepository
) : PictureInteractor {
    override suspend fun getPhotoByDate(date: Date): Flow<NasaPicture> {
        return pictureRepository.getPhotoByDate(date)
    }
}
