package ViewModels.Factories

import Repository.Database.DAOs.MovieDetailsDAO
import ViewModels.MovieDetailsViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieDetailsVMFactory (
    private val id:Int,
    private val dataSource: MovieDetailsDAO,
    private val application: Application):ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)){
            return MovieDetailsViewModel(id,dataSource,application)as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}