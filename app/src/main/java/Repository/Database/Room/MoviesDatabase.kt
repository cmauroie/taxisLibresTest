package Repository.Database.Room

import Repository.Database.DAOs.MovieDAO
import Repository.Database.DAOs.MovieDetailsDAO
import Repository.Database.EntitiesDB.MovieDetailEntity
import Repository.Database.EntitiesDB.MovieEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class, MovieDetailEntity::class],
    version = 4,
    exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
    abstract fun movieDetailsDAO(): MovieDetailsDAO
//ALLOW ACCESSING THE OBJECT WITHOUT AN INSTANCE
    companion object {
        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        fun getInstance(context: Context): MoviesDatabase {
            //ALLOW ONLY ONE THEAD AT TIME TO ACCESS
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MoviesDatabase::class.java,
                        "movies_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}