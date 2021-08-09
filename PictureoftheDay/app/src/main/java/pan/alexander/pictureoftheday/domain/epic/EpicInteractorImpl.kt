package pan.alexander.pictureoftheday.domain.epic

import kotlinx.coroutines.flow.Flow
import pan.alexander.pictureoftheday.domain.epic.entities.EpicImage
import javax.inject.Inject

class EpicInteractorImpl @Inject constructor(
    private val epicRepository: EpicRepository
) : EpicInteractor {
    override suspend fun getRecentEnhancedColorImage(): Flow<EpicImage> {
        return epicRepository.getRecentEnhancedColorImage()
    }
}
