package pan.alexander.pictureoftheday.data.epic

import pan.alexander.pictureoftheday.data.epic.model.EpicServerResponse
import retrofit2.Response

interface EpicDataSource {
    suspend fun getRecentEnhancedColorImage(): Response<List<EpicServerResponse>>
}
