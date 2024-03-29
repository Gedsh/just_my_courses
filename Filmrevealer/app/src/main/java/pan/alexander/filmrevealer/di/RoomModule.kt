package pan.alexander.filmrevealer.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pan.alexander.filmrevealer.database.*
import javax.inject.Singleton

@Module
class RoomModule(appContext: Application) {
    private val appDatabase = Room
        .databaseBuilder(appContext, AppDatabase::class.java, "main_database")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase = appDatabase

    @Provides
    @Singleton
    fun provideFilmDao(): FilmDao = appDatabase.filmDao()

    @Provides
    @Singleton
    fun provideFilmDetailsDao(): FilmDetailsDao = appDatabase.filmDetailsDao()
}
