package pan.alexander.filmrevealer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pan.alexander.filmrevealer.domain.entities.Film
import pan.alexander.filmrevealer.domain.entities.FilmDetails

@Database(entities = [Film::class, FilmDetails::class], version = 8)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun filmDetailsDao(): FilmDetailsDao
}
