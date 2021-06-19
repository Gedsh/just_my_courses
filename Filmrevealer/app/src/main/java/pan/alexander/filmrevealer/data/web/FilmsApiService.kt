package pan.alexander.filmrevealer.data.web

import pan.alexander.filmrevealer.data.web.pojo.FilmsPage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmsApiService {
    @GET("3/movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<FilmsPage>

    @GET("3/movie/upcoming")
    fun getUpcoming(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<FilmsPage>

    @GET("3/movie/top_rated")
    fun getTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<FilmsPage>

    @GET("3/movie/popular")
    fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<FilmsPage>
}
