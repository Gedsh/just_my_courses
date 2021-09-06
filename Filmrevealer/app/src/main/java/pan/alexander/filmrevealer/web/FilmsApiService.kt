package pan.alexander.filmrevealer.web

import io.reactivex.rxjava3.core.Single
import pan.alexander.filmrevealer.data.remote.pojo.*
import retrofit2.Response
import retrofit2.http.*

interface FilmsApiService {
    @GET("3/movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Single<Response<FilmsPageJson>>

    @GET("3/movie/upcoming")
    fun getUpcoming(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Single<Response<FilmsPageJson>>

    @GET("3/movie/top_rated")
    fun getTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Single<Response<FilmsPageJson>>

    @GET("3/movie/popular")
    fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Single<Response<FilmsPageJson>>

    @GET("3/movie/{movie_id}")
    fun getPreciseDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Single<Response<FilmPreciseDetailsJson>>

    @GET("3/authentication/guest_session/new")
    fun createGuestSession(
        @Query("api_key") apiKey: String
    ): Single<Response<GuestSession>>

    @GET("3/guest_session/{guest_session_id}/rated/movies?sort_by=created_at.asc")
    fun getRatedByUser(
        @Path("guest_session_id") guestSessionId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Single<Response<FilmsPageJson>>

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("3/movie/{movie_id}/rating")
    fun rateFilm(
        @Path("movie_id") movieId: Int,
        @Body body: RateBody,
        @Query("api_key") apiKey: String,
        @Query("guest_session_id") guestSessionId: String,
    ): Single<Response<ServerResponse>>
}
