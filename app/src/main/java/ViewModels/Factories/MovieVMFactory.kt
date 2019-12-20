package ViewModels.Factories

import Repository.Database.DAOs.MovieDAO
import ViewModels.MovieViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieVMFactory (
    private val dataSource: MovieDAO,
    private val application: Application): ViewModelProvider.Factory{
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MovieViewModel::class.java)){
                return MovieViewModel(dataSource,application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}