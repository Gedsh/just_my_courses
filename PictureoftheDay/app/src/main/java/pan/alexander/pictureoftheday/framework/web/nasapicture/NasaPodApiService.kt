package pan.alexander.pictureoftheday.framework.web.nasapicture

import androidx.annotation.Keep
import pan.alexander.pictureoftheday.data.picture.model.ApiServerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@Keep
interface NasaPodApiService {
    @GET("planetary/apod")
    suspend fun getPictureByDateStr(
        @Query("api_key") apiKey: String,
        @Query("date") dateStr: String
    ): Response<ApiServerResponse>
}
