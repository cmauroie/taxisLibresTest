package Repository.Database.DAOs

import Repository.Database.EntitiesDB.MovieEntity
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDAO {
    @Insert
    fun insert(movies: List<MovieEntity>)
    @Query("select * from movie_table order by title")
    fun getAllMovies(): LiveData<List<MovieEntity>>
    @Query("delete from movie_table")
    fun clear()
}