package col.rgm.taxislibrestest.Adapters

import Repository.RemoteDataSource.Models.MovieDTO
import Utilities.Util
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import col.rgm.taxislibrestest.MovieDetailActivity
import col.rgm.taxislibrestest.R
import com.squareup.picasso.Picasso
import java.lang.Exception


class RecyclerViewMovieAdapter(private val context: Context, private val movies: MovieDTO) :
    RecyclerView.Adapter<RecyclerViewMovieAdapter.ViewHolder>() {
    val utilities=Util()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_child, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movies.results.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val imgPath=movies.results.get(position).poster_path
            Picasso
                .get()
                .load(utilities.URL_IMAGES_THE_MOVIE_DB+utilities.IMAGE_SIZE+imgPath)
                .placeholder(R.drawable.imagen_vacia)
                .error(R.drawable.imagen_vacia)
                .into(holder.imagenPeli!!)
            holder.tituloPeli!!.text = movies.results.get(position).title
            holder.ratingPeli!!.text =
                context.resources.getString(R.string.rating) + " " + movies.results.get(position).vote_average.toString()
            holder.fechaEstrenoPeli!!.text =
                context.resources.getString(R.string.fechaEstreno) + " " + movies.results.get(
                    position
                ).release_date
            holder.contenedorPelis.setTag(position)
            holder.contenedorPelis.setOnClickListener { v: View? ->
                try {
                    val intent = Intent(context, MovieDetailActivity::class.java)
                    intent.putExtra(Util().KEY_ID_MOVIE, movies.results[v!!.tag as Int].id)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.applicationContext.startActivity(intent)
                }
                catch (e:Exception){
                    Log.i("asd","error")
                }
            }
        }catch (e:Exception){
            Log.i(Util().TAG_REC_V_A,context.resources.getString(R.string.error_rec_view_adap))
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val contenedorPelis: LinearLayout =
            v.findViewById<View>(R.id.contenedorPelicula) as LinearLayout
        var imagenPeli: ImageView = v.findViewById<View>(R.id.imagen_peli) as ImageView
        var tituloPeli: TextView = v.findViewById<View>(R.id.titulo_pelicula) as TextView
        var ratingPeli: TextView = v.findViewById<View>(R.id.votos_pelicula) as TextView
        var fechaEstrenoPeli: TextView = v.findViewById<View>(R.id.fecha_pelicula) as TextView
    }

}