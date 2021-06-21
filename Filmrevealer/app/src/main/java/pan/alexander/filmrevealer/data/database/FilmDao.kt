package pan.alexander.filmrevealer.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import pan.alexander.filmrevealer.domain.entities.Film

@Dao
interface FilmDao {
    @Query("SELECT * FROM Film WHERE section = :section ORDER BY page")
    fun getFilmsForSection(section: Int): LiveData<List<Film>>

    @Query("SELECT * FROM Film WHERE is_liked = 1")
    fun getLikedFilms(): LiveData<List<Film>>

    @Query("DELETE FROM Film WHERE section = :section AND page = :page AND is_liked = 0")
    fun deleteAllFilmsFromSection(section: Int, page: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Film)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilms(films: List<Film>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(film: Film)

    @Delete
    fun delete(film: Film)
}
