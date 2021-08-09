package pan.alexander.pictureoftheday.data.mars

import pan.alexander.pictureoftheday.data.mars.model.MarsServerResponse
import retrofit2.Response

interface MarsPhotosDataSource {
    suspend fun getCuriosityPhotosBySol(sol: Int): Response<MarsServerResponse>
}
