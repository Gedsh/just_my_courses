package pan.alexander.filmrevealer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pan.alexander.filmrevealer.domain.entities.Film

@Database(entities = [Film::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}
