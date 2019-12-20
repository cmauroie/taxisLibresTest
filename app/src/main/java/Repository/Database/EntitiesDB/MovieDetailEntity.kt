package Repository.Database.EntitiesDB

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_detail_table")
data class MovieDetailEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    @ColumnInfo(name="id_pelicula")
    var idPelicula: Int,
    var overview:String,
    @ColumnInfo(name="original_title")
    var originalTitle:String,
    @ColumnInfo(name="poster_path")
    var posterPath: ByteArray?,
    @ColumnInfo(name="backdrop_path")
    var backdropPath:ByteArray?,
    @ColumnInfo(name="release_date")
    var releaseDate: String,
    var revenue: String,
    var tagline: String,
    var title: String,
    @ColumnInfo(name="vote_average")
    var voteAverage: String
    )