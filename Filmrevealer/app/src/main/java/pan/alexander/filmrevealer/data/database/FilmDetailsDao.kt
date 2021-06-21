package pan.alexander.filmrevealer.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import pan.alexander.filmrevealer.domain.entities.FilmDetails

@Dao
interface FilmDetailsDao {
    @Query("SELECT * FROM FilmDetails WHERE movie_id = :id")
    fun getFilmDetailsById(id: Int): LiveData<List<FilmDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmDetails(details: FilmDetails)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFilmDetails(details: FilmDetails)

    @Query("DELETE FROM FilmDetails WHERE timestamp < :timestamp")
    fun deleteFilmsDetailsOlderTimestamp(timestamp: Long)
}
