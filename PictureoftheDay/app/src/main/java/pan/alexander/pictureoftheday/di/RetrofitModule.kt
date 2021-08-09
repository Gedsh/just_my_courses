package pan.alexander.pictureoftheday.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pan.alexander.pictureoftheday.BuildConfig
import pan.alexander.pictureoftheday.framework.web.epic.NasaEpicApiService
import pan.alexander.pictureoftheday.framework.web.nasaimage.NasaApiService
import pan.alexander.pictureoftheday.utils.configuration.ConfigurationManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val CALL_TIMEOUT_SEC = 10L

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideNasaServiceApi(
        @Named("NASA") retrofit: Retrofit
    ): NasaApiService =
        retrofit.create(NasaApiService::class.java)

    @Provides
    @Singleton
    fun provideEpicServiceApi(
        @Named("EPIC") retrofit: Retrofit
    ): NasaEpicApiService =
        retrofit.create(NasaEpicApiService::class.java)

    @Named("EPIC")
    @Provides
    @Singleton
    fun provideEpicRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        configurationManager: ConfigurationManager
    ): Retrofit = Retrofit.Builder()
        .baseUrl(configurationManager.getEpicBaseUrl())
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

    @Named("NASA")
    @Provides
    @Singleton
    fun provideNasaRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        configurationManager: ConfigurationManager
    ): Retrofit = Retrofit.Builder()
        .baseUrl(configurationManager.getNasaBaseUrl())
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
