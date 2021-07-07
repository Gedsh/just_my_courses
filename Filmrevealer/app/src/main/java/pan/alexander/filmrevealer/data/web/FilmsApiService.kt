package pan.alexander.filmrevealer.data.web

import pan.alexander.filmrevealer.data.web.pojo.*
import retrofit2.Call
import retrofit2.http.*

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

    @GET("3/authentication/guest_session/new")
    fun createGuestSession(
        @Query("api_key") apiKey: String
    ): Call<GuestSession>

    @GET("3/guest_session/{guest_session_id}/rated/movies?sort_by=created_at.asc")
    fun getRatedByUser(
        @Path("guest_session_id") guestSessionId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<FilmsPageJson>

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("3/movie/{movie_id}/rating")
    fun rateFilm(
        @Path("movie_id") movieId: Int,
        @Body body: RateBody,
        @Query("api_key") apiKey: String,
        @Query("guest_session_id") guestSessionId: String,
    ): Call<ServerResponse>
}
