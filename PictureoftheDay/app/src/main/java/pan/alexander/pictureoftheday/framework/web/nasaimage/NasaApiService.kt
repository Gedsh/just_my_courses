package pan.alexander.pictureoftheday.framework.web.nasaimage

import androidx.annotation.Keep
import pan.alexander.pictureoftheday.data.mars.model.MarsServerResponse
import pan.alexander.pictureoftheday.data.picture.model.ApiServerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Keep
interface NasaApiService {
    @GET("planetary/apod")
    suspend fun getPictureOfTheDayByDateStr(
        @Query("api_key") apiKey: String,
        @Query("date") dateStr: String
    ): Response<ApiServerResponse>

    @GET("mars-photos/api/v1/rovers/{rover}/photos")
    suspend fun getMarsPhotosByRoverAndSol(
        @Path("rover") rover: String,
        @Query("sol") sol: Int,
        @Query("api_key") apiKey: String,
    ): Response<MarsServerResponse>
}
