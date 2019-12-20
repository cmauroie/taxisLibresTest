package col.rgm.taxislibrestest

import Repository.Database.Room.MoviesDatabase
import Repository.RemoteDataSource.ConnectionService
import Repository.RemoteDataSource.Models.MovieDetailDTO
import Repository.RemoteDataSource.URLService
import Utilities.Util
import ViewModels.Factories.MovieDetailsVMFactory
import ViewModels.MovieDetailsViewModel
import android.app.ActionBar
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import col.rgm.taxislibrestest.databinding.ActivityMovieDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.ByteBuffer
import java.util.*
import kotlin.concurrent.schedule


class MovieDetailActivity : AppCompatActivity() {
    private val utilities = Util()
    private lateinit var binding: ActivityMovieDetailBinding
    private val connectionService = ConnectionService()
    private var idPeli: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        val dataSource = MoviesDatabase.getInstance(this).movieDetailsDAO()
        idPeli = intent.getIntExtra(utilities.KEY_ID_MOVIE, 0)
        val application = requireNotNull(application)
        val viewModelFactory = MovieDetailsVMFactory(idPeli, dataSource, application)
        binding.movieDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel::class.java)
        //Syncronizes the viewmodel data and the UI
        binding.lifecycleOwner = this
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
        fetchMovieInfo()
    }

    private fun fetchMovieInfo() {
        val retrofit = connectionService.getRetrofit(utilities.URL_THE_MOVIE_DB)
        val service = retrofit.create(URLService::class.java)
        val call = service.getMovieById(
            idPeli,
            resources.getString(R.string.the_movie_db_api),
            utilities.LANGUAGE_ESPAÑOL
        )
        call.enqueue(object : Callback<MovieDetailDTO> {
            override fun onFailure(call: Call<MovieDetailDTO>, t: Throwable) {
                Log.i(utilities.TAG_RETROFIT, t.message)
                Toast.makeText(
                    applicationContext,
                    resources.getText(R.string.error_the_movie_db),
                    Toast.LENGTH_LONG
                ).show()
                //LOADS CACHE VERSION FROM DB
                binding.apply {
                    binding.movieDetailViewModel!!.loadFromDB()
                    if (movieDetailViewModel!!.poster_path.value != null) {
                        imagen_portada.setImageBitmap(
                            BitmapFactory.decodeByteArray(
                                movieDetailViewModel!!.poster_path.value,
                                0,
                                movieDetailViewModel!!.poster_path.value!!.size
                            )
                        )
                    }
                    if (movieDetailViewModel!!.backdrop_path.value != null) {
                        imagenPortadaTrasera.setImageBitmap(
                            BitmapFactory.decodeByteArray(
                                movieDetailViewModel!!.backdrop_path.value,
                                0,
                                movieDetailViewModel!!.backdrop_path.value!!.size
                            )
                        )
                    }

                    invalidateAll()
                }


            }

            override fun onResponse(
                call: Call<MovieDetailDTO>,
                response: Response<MovieDetailDTO>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        val dto = response.body()!!
                        binding.apply {
                            var asyncTask = TratarImagenes()

                            var result = asyncTask.execute(
                                arrayOf<String>(
                                    dto.poster_path,
                                    dto.backdrop_path
                                )
                            ).get()
                            if (result!![0] != null) {
                                movieDetailViewModel!!.poster_path.value = result!![0]
                            }
                            if (result!![1] != null) {
                                movieDetailViewModel!!.backdrop_path.value = result!![1]
                            }

                            //SETS THE IMAGE DOWLOADED FROM THE URL INTO THE VIEW
                            Picasso.get()
                                .load(utilities.URL_IMAGES_THE_MOVIE_DB + utilities.IMAGE_SIZE + dto.poster_path)
                                .placeholder(R.drawable.imagen_vacia)
                                .error(R.drawable.imagen_vacia)
                                .into(imagen_portada)

                            Picasso.get()
                                .load(utilities.URL_IMAGES_THE_MOVIE_DB + utilities.IMAGE_SIZE + dto.backdrop_path)
                                .placeholder(R.drawable.imagen_vacia)
                                .error(R.drawable.imagen_vacia)
                                .into(imagen_portada_trasera)
                            movieDetailViewModel!!.titulo_original.value = dto.original_title
                            movieDetailViewModel!!.titulo_traducido.value = dto.title
                            movieDetailViewModel!!.tag_pelicula.value = dto.tagline
                            movieDetailViewModel!!.ganancia_pelicula.value =
                                "$ " + dto.revenue.toString()
                            movieDetailViewModel!!.rating_pelicula.value =
                                dto.vote_average.toString()
                            movieDetailViewModel!!.fecha_estreno.value = dto.release_date
                            movieDetailViewModel!!.resumen.value = dto.overview
                            //SAVES TO INTERNAL DB
                            movieDetailViewModel!!.saveToDB()
                            invalidateAll()
                        }
                    } else {

                        Toast.makeText(
                            applicationContext,
                            resources.getText(R.string.error_sin_datos),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    class TratarImagenes : AsyncTask<Array<String>, Void, Array<ByteArray?>>() {

        private val utilities = Util()
        override fun doInBackground(vararg params: Array<String>?): Array<ByteArray?> {
            var imagenP: ByteArray? = null
            var imagenPT: ByteArray? = null
            var imagenPortada: Bitmap? = null
            var imagenPortadaTrasera: Bitmap? = null
            //DOWNLOADS THE IMAGE INTO A BITMAP
            if (params[0]!!.get(0) != "") {
                imagenPortada = Picasso.get()
                    .load(
                        utilities.URL_IMAGES_THE_MOVIE_DB + utilities.IMAGE_SIZE + params[0]!!.get(
                            0
                        )
                    )
                    .get()
            }
            if (params[0]!!.get(1) != "") {
                imagenPortadaTrasera = Picasso.get()
                    .load(
                        utilities.URL_IMAGES_THE_MOVIE_DB + utilities.IMAGE_SIZE + params[0]!!.get(
                            1
                        )
                    )
                    .get()
            }
            if (imagenPortada != null) {
                imagenP = imagenPortada.convertToByteArray()
            }
            if (imagenPortadaTrasera != null) {
                imagenPT = imagenPortadaTrasera.convertToByteArray()
            }
            return arrayOf(imagenP, imagenPT)
        }

        fun Bitmap.convertToByteArray(): ByteArray {
            //minimum number of bytes that can be used to store this bitmap's pixels
            val size = this.byteCount

            //allocate new instances which will hold bitmap
            val buffer = ByteBuffer.allocate(size)
            val bytes = ByteArray(size)

            //copy the bitmap's pixels into the specified buffer
            this.copyPixelsToBuffer(buffer)

            //rewinds buffer (buffer position is set to zero and the mark is discarded)
            buffer.rewind()

            //transfer bytes from buffer into the given destination array
            buffer.get(bytes)

            //return bitmap's pixels
            return bytes
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, MovieActivity::class.java))
    }
}