package pan.alexander.pictureoftheday.domain.epic

import kotlinx.coroutines.flow.Flow
import pan.alexander.pictureoftheday.domain.epic.entities.EpicImage

interface EpicInteractor {
    suspend fun getRecentEnhancedColorImage(): Flow<EpicImage>
}
