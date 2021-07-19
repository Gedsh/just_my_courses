package pan.alexander.filmrevealer.data.web

import pan.alexander.filmrevealer.data.web.pojo.*
import retrofit2.Response
import retrofit2.http.*

interface FilmsApiService {
    @GET("3/movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Response<FilmsPageJson>

    @GET("3/movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Response<FilmsPageJson>

    @GET("3/movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Response<FilmsPageJson>

    @GET("3/movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Response<FilmsPageJson>

    @GET("3/movie/{movie_id}")
    suspend fun getPreciseDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Response<FilmPreciseDetailsJson>

    @GET("3/authentication/guest_session/new")
    suspend fun createGuestSession(
        @Query("api_key") apiKey: String
    ): Response<GuestSession>

    @GET("3/guest_session/{guest_session_id}/rated/movies?sort_by=created_at.asc")
    suspend fun getRatedByUser(
        @Path("guest_session_id") guestSessionId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Response<FilmsPageJson>

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("3/movie/{movie_id}/rating")
    suspend fun rateFilm(
        @Path("movie_id") movieId: Int,
        @Body body: RateBody,
        @Query("api_key") apiKey: String,
        @Query("guest_session_id") guestSessionId: String,
    ): Response<ServerResponse>
}
