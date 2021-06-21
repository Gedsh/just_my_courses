package pan.alexander.filmrevealer.data.web

import pan.alexander.filmrevealer.data.web.pojo.FilmPreciseDetailsJson
import pan.alexander.filmrevealer.data.web.pojo.FilmsPageJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmsApiService {
    @GET("3/movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<FilmsPageJson>

    @GET("3/movie/upcoming")
    fun getUpcoming(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<FilmsPageJson>

    @GET("3/movie/top_rated")
    fun getTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<FilmsPageJson>

    @GET("3/movie/popular")
    fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<FilmsPageJson>

    @GET("3/movie/{movie_id}")
    fun getPreciseDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<FilmPreciseDetailsJson>
}
