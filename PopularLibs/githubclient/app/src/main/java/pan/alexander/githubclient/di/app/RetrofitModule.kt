package pan.alexander.githubclient.di.app

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pan.alexander.githubclient.BuildConfig
import pan.alexander.githubclient.utils.configuration.ConfigurationManager
import pan.alexander.githubclient.web.GithubUserApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CALL_TIMEOUT_SEC = 10L

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideServiceApi(retrofit: Retrofit): GithubUserApi =
        retrofit.create(GithubUserApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        rxCallAdapterFactory: RxJava3CallAdapterFactory,
        gsonConverterFactory: GsonConverterFactory,
        configurationManager: ConfigurationManager
    ): Retrofit = Retrofit.Builder()
        .baseUrl(configurationManager.getBaseUrl())
        .addCallAdapterFactory(rxCallAdapterFactory)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideRx3CallAdapterFactory(): RxJava3CallAdapterFactory =
        RxJava3CallAdapterFactory.createSynchronous()

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
