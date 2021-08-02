package pan.alexander.pictureoftheday.data.picture

import pan.alexander.pictureoftheday.data.picture.model.ApiServerResponse
import retrofit2.Response

interface PictureDataSource {
    suspend fun getPhotoByDate(dateStr: String): Response<ApiServerResponse>
}
