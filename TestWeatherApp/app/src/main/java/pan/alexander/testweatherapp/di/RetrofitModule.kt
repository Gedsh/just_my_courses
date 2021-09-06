package pan.alexander.testweatherapp.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import pan.alexander.testweatherapp.BuildConfig
import pan.alexander.testweatherapp.framework.web.CurrentWeatherApiService
import pan.alexander.testweatherapp.utils.configuration.ConfigurationManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CALL_TIMEOUT_SEC = 10L

object RetrofitModule {

    fun provideServiceApi() = module {
        single { provideApi(retrofit = get()) }
        single {
            provideRetrofit(
                okHttpClient = get(),
                gsonConverterFactory = get(),
                configurationManager = get()
            )
        }
        single { provideClient(httpLoggingInterceptor = get()) }
        single { provideGsonConverterFactory() }
        single { provideInterceptor() }
    }

    private fun provideApi(
        retrofit: Retrofit
    ): CurrentWeatherApiService =
        retrofit.create(CurrentWeatherApiService::class.java)

    private fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        configurationManager: ConfigurationManager
    ): Retrofit = Retrofit.Builder()
        .baseUrl(configurationManager.getBaseUrl())
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

    private fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    private fun provideInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private fun provideClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(CALL_TIMEOUT_SEC, TimeUnit.SECONDS).apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(httpLoggingInterceptor)
                }
            }.build()
}
