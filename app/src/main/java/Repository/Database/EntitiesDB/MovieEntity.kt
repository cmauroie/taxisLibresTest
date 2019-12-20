package Repository.Database.EntitiesDB

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long=0L,
    var page: Integer,
    @ColumnInfo(name="total_results")
    var totalResults: Integer,
    @ColumnInfo(name="total_pages")
    var totalPages: Integer,
    @ColumnInfo(name="poster_path")
    var posterPath: ByteArray?,
    @ColumnInfo(name="id_movie")
    var idMovie: Integer,
    var title: String,
    @ColumnInfo(name="vote_average")
    var voteAverage: Float,
    @ColumnInfo(name="release_date")
    var releaseDate: String
)