package pan.alexander.pictureoftheday.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pan.alexander.pictureoftheday.BuildConfig
import pan.alexander.pictureoftheday.framework.web.nasapicture.NasaPodApiService
import pan.alexander.pictureoftheday.utils.configuration.ConfigurationManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CALL_TIMEOUT_SEC = 10L

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideServiceApi(retrofit: Retrofit): NasaPodApiService =
        retrofit.create(NasaPodApiService::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        configurationManager: ConfigurationManager
    ): Retrofit = Retrofit.Builder()
        .baseUrl(configurationManager.getBaseUrl())
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
