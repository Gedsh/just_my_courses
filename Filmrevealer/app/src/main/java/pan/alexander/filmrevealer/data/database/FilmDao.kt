package pan.alexander.filmrevealer.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import pan.alexander.filmrevealer.domain.entities.Film

@Dao
interface FilmDao {
    @Query("SELECT * FROM Film WHERE section = :section ORDER BY page")
    fun getFilmsForSection(section: Int): LiveData<List<Film>>

    @Query("SELECT * FROM Film WHERE section = :section ORDER BY id DESC")
    fun getFilmsForSectionOrderByAdding(section: Int): LiveData<List<Film>>

    @Query("SELECT * FROM Film WHERE section = :ratingSection AND movie_id = :movieId")
    fun getRatedFilmById(movieId: Int, ratingSection: Int): LiveData<List<Film>>

    @Query("SELECT * FROM Film WHERE section = :likedSection AND movie_id = :movieId")
    fun getLikedFilmById(movieId: Int, likedSection: Int): Film?

    @Query("SELECT movie_id FROM Film WHERE section = :likedSection")
    fun getLikedImdbIds(likedSection: Int): LiveData<List<Int>>

    @Query("DELETE FROM Film WHERE section = :section AND page = :page")
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
