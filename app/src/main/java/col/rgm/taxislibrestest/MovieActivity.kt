package col.rgm.taxislibrestest

import Repository.Database.Room.MoviesDatabase
import Repository.RemoteDataSource.Models.MovieDTO
import Repository.RemoteDataSource.ConnectionService
import Repository.RemoteDataSource.URLService
import Utilities.Util
import ViewModels.Factories.MovieVMFactory
import ViewModels.MovieViewModel
import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import col.rgm.taxislibrestest.Adapters.RecyclerViewMovieAdapter
import kotlinx.android.synthetic.main.activity_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MovieActivity : AppCompatActivity() {
    private val connectionService = ConnectionService()
    private val utilities = Util()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_movies)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val layoutParams = androidx.appcompat.app.ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.custom_action_bar, null)
        supportActionBar!!.setCustomView(view, layoutParams)
        val parent = view.getParent() as Toolbar
        parent.setContentInsetsAbsolute(0, 0)
        //val dataSource = MoviesDatabase.getInstance(this).movieDAO()
        //val application = requireNotNull(application)
        //val viewModelFactory = MovieVMFactory(dataSource, application)
        //binding.movieDetailViewModel =
         //   ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel::class.java)
        obtainMovieList()

    }

    private fun obtainMovieList() {
        val retrofit = connectionService.getRetrofit(utilities.URL_THE_MOVIE_DB)
        val service = retrofit.create(URLService::class.java)
        val call = service.getListPopularMovies(
            resources.getString(R.string.the_movie_db_api),
            utilities.LANGUAGE_ESPAÑOL
        )
        call.enqueue(object : Callback<MovieDTO> {
            override fun onFailure(call: Call<MovieDTO>, t: Throwable) {
                Log.i(utilities.TAG_RETROFIT, t.message!!)
                Toast.makeText(
                    applicationContext,
                    resources.getText(R.string.error_the_movie_db),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<MovieDTO>, response: Response<MovieDTO>) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        recyclerViewPeliculas.adapter =
                            RecyclerViewMovieAdapter(applicationContext, response.body()!!)
                        recyclerViewPeliculas.layoutManager =
                            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
                    }
                }
            }

        })
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}