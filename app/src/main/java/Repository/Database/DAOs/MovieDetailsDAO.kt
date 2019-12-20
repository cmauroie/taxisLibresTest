package Repository.Database.DAOs

import Repository.Database.EntitiesDB.MovieDetailEntity
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDetailsDAO {
    @Insert
    fun insertMovieDetails(movies: MovieDetailEntity):Long
    @Query("select * from movie_detail_table")
    fun getAllMovieDetals(): List<MovieDetailEntity>
    @Query("select * from movie_detail_table where id_pelicula = :id")
    fun getMovieDetailsById(id:Int): MovieDetailEntity
    @Query("delete from movie_detail_table")
    fun deleteTable()
}