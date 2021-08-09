package pan.alexander.pictureoftheday.domain.mars

import kotlinx.coroutines.flow.Flow
import pan.alexander.pictureoftheday.domain.mars.entities.MarsPhoto

interface MarsRepository {
    fun getCuriosityPhotoBySol(sol: Int): Flow<MarsPhoto>
}
