package pan.alexander.pictureoftheday.framework.web.epic

import androidx.annotation.Keep
import pan.alexander.pictureoftheday.data.epic.model.EpicServerResponse
import retrofit2.Response
import retrofit2.http.GET

@Keep
interface NasaEpicApiService {
    @GET("api/enhanced")
    suspend fun getRecentEnhancedColorImage(
    ): Response<List<EpicServerResponse>>
}
