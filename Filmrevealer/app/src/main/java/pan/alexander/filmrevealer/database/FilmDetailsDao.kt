package pan.alexander.filmrevealer.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import pan.alexander.filmrevealer.domain.entities.FilmDetails

@Dao
interface FilmDetailsDao {
    @Query("SELECT * FROM FilmDetails WHERE movie_id = :id")
    fun getFilmDetailsById(id: Int): Flowable<List<FilmDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmDetails(details: FilmDetails): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFilmDetails(details: FilmDetails): Completable

    @Query("DELETE FROM FilmDetails WHERE timestamp < :timestamp")
    fun deleteFilmsDetailsOlderTimestamp(timestamp: Long): Completable
}
