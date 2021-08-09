package pan.alexander.pictureoftheday.data.epic

import kotlinx.coroutines.withContext
import pan.alexander.pictureoftheday.data.epic.model.EpicServerResponse
import pan.alexander.pictureoftheday.framework.web.epic.NasaEpicApiService
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class EpicDataSourceImpl @Inject constructor(
    private val nasaEpicApiService: NasaEpicApiService,
    private val dispatcherIO: CoroutineContext
) : EpicDataSource {
    override suspend fun getRecentEnhancedColorImage(): Response<List<EpicServerResponse>> {
        return withContext(dispatcherIO) {
            nasaEpicApiService.getRecentEnhancedColorImage()
        }
    }
}
