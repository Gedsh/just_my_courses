package pan.alexander.pictureoftheday.domain.epic

import kotlinx.coroutines.flow.Flow
import pan.alexander.pictureoftheday.domain.epic.entities.EpicImage

interface EpicRepository {
    suspend fun getRecentEnhancedColorImage(): Flow<EpicImage>
}
