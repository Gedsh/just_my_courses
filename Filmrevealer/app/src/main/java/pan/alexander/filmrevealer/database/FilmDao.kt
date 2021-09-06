package pan.alexander.filmrevealer.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import pan.alexander.filmrevealer.domain.entities.Film

@Dao
interface FilmDao {
    @Query("SELECT * FROM Film WHERE section = :section ORDER BY page")
    fun getFilmsForSection(section: Int): Flowable<List<Film>>

    @Query("SELECT * FROM Film WHERE section = :section ORDER BY id DESC")
    fun getFilmsForSectionOrderByAdding(section: Int): Flowable<List<Film>>

    @Query("SELECT * FROM Film WHERE section = :ratingSection AND movie_id = :movieId")
    fun getRatedFilmById(movieId: Int, ratingSection: Int): Flowable<List<Film>>

    @Query("SELECT * FROM Film WHERE section = :likedSection AND movie_id = :movieId")
    fun getLikedFilmById(movieId: Int, likedSection: Int): Single<List<Film>>

    @Query("SELECT movie_id FROM Film WHERE section = :likedSection")
    fun getLikedImdbIds(likedSection: Int): Flowable<List<Int>>

    @Query("DELETE FROM Film WHERE section = :section AND page = :page")
    fun deleteAllFilmsFromSection(section: Int, page: Int): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Film): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilms(films: List<Film>): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(film: Film): Completable

    @Delete
    fun delete(film: Film): Completable
}
