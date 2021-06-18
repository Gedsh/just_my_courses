package pan.alexander.filmrevealer.di

import dagger.Module
import dagger.Provides
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.data.web.FilmsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideServiceApi(retrofit: Retrofit): FilmsApiService {
        return retrofit.create(FilmsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(App.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()
}
