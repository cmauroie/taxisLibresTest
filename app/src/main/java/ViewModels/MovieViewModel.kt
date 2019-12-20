package ViewModels

import Repository.Database.DAOs.MovieDAO
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MovieViewModel(val datasource: MovieDAO,
                     application: Application
) : AndroidViewModel(application){

}