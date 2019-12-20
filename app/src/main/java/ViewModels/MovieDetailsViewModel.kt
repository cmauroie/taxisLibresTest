package ViewModels

import Repository.Database.DAOs.MovieDetailsDAO
import Repository.Database.EntitiesDB.MovieDetailEntity
import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class MovieDetailsViewModel(
    val id: Int,
    val datasource: MovieDetailsDAO,
    application: Application
) : AndroidViewModel(
    application
) {
    //Allow control over a process, like cancel
    private var viewModelJob = Job()
    //Updates the main thread ui after finished
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var titulo_original = MutableLiveData<String>("")
    var titulo_traducido = MutableLiveData<String>("")
    var tag_pelicula = MutableLiveData<String>("")
    var ganancia_pelicula = MutableLiveData<String>("")
    var rating_pelicula = MutableLiveData<String>("")
    var fecha_estreno = MutableLiveData<String>("")
    var resumen = MutableLiveData<String>("")
    var poster_path = MutableLiveData<ByteArray?>(null)
    var backdrop_path = MutableLiveData<ByteArray?>(null)


    fun loadFromDB() {
        uiScope.launch {
            var movieDetailsObtained = getMovieDetailsFromDB()
            if (movieDetailsObtained != null) {
                titulo_original.value = movieDetailsObtained.originalTitle
                titulo_traducido.value = movieDetailsObtained.title
                tag_pelicula.value = movieDetailsObtained.tagline
                ganancia_pelicula.value = movieDetailsObtained.revenue
                rating_pelicula.value = movieDetailsObtained.voteAverage
                fecha_estreno.value = movieDetailsObtained.releaseDate
                resumen.value = movieDetailsObtained.overview
                poster_path.value = movieDetailsObtained.posterPath
                backdrop_path.value = movieDetailsObtained.backdropPath
            }
        }
    }
    fun getEntity() {
        uiScope.launch {
            getMovieDetailsFromDB()
        }
    }fun getAllEntity() {
       uiScope.launch {
            getAllMovieDetailsFromDB()
        }
    }
    fun deleteAll(){
        uiScope.launch {
            deleteAllMovieDetailsFromDB()
        }
    }
    fun saveToDB(){
        uiScope.launch {
            var movieDetailEntity = MovieDetailEntity(
                idPelicula = id,
                originalTitle = titulo_original.value!!,
                title = titulo_traducido.value!!,
                tagline = tag_pelicula.value!!,
                revenue = ganancia_pelicula.value!!,
                voteAverage = rating_pelicula.value!!,
                releaseDate = fecha_estreno.value!!,
                overview = resumen.value!!,
                posterPath = poster_path.value,
                backdropPath = backdrop_path.value
            )
            insertMovieDetailsToDB(movieDetailEntity)
        }
    }
    private suspend fun deleteAllMovieDetailsFromDB() {
        return withContext(Dispatchers.IO) {
            datasource.deleteTable()
        }
    }
    private suspend fun getAllMovieDetailsFromDB(): List<MovieDetailEntity> {
        return withContext(Dispatchers.IO) {
            datasource.getAllMovieDetals()
        }
    }
    private suspend fun getMovieDetailsFromDB(): MovieDetailEntity? {
        return withContext(Dispatchers.IO) {
            datasource.getMovieDetailsById(id)
        }
    }
    private suspend fun insertMovieDetailsToDB(movieDetailEntity: MovieDetailEntity):Long {
        return withContext(Dispatchers.IO) {
             datasource.insertMovieDetails(movieDetailEntity)
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}