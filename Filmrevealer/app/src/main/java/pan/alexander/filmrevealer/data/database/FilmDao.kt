package pan.alexander.filmrevealer.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import pan.alexander.filmrevealer.domain.entities.Film

@Dao
interface FilmDao {
    @Query("SELECT * FROM Film WHERE section = :section")
    fun getFilmsForSection(section: Int): LiveData<List<Film>>

    @Query("SELECT * FROM Film WHERE is_liked = 1")
    fun getLikedFilms(): LiveData<List<Film>>

    @Query("SELECT * FROM Film WHERE id == :id")
    fun getFilmById(id: Int): Film

    @Query("DELETE FROM Film WHERE section == :section AND is_liked == 0")
    fun deleteAllFilmsFromSection(section: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Film)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(film: Film)

    @Delete
    fun delete(film: Film)
}
