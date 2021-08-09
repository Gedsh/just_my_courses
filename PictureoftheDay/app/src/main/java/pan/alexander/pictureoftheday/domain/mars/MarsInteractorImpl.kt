package pan.alexander.pictureoftheday.domain.mars

import kotlinx.coroutines.flow.Flow
import pan.alexander.pictureoftheday.domain.mars.entities.MarsPhoto
import javax.inject.Inject

class MarsInteractorImpl @Inject constructor(
    private val marsRepository: MarsRepository
) : MarsInteractor {
    override fun getCuriosityPhotoBySol(sol: Int): Flow<MarsPhoto> {
        return marsRepository.getCuriosityPhotoBySol(sol)
    }
}
