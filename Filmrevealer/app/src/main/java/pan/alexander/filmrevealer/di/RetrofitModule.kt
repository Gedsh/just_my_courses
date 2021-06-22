package pan.alexander.filmrevealer.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.BuildConfig
import pan.alexander.filmrevealer.data.web.FilmsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CALL_TIMEOUT_SEC = 10L

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideServiceApi(retrofit: Retrofit): FilmsApiService =
        retrofit.create(FilmsApiService::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(App.BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(CALL_TIMEOUT_SEC, TimeUnit.SECONDS).apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(httpLoggingInterceptor)
                }
            }.build()
}
