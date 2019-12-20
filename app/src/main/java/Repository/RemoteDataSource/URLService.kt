package Repository.RemoteDataSource

import Repository.RemoteDataSource.Models.GoogleDirectionsDTO
import Repository.RemoteDataSource.Models.MovieDTO
import Repository.RemoteDataSource.Models.MovieDetailDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface URLService{
    //Obtains the Google directions based on lat-long
    @GET("maps/api/directions/json?")
    fun getDirections(@Query("origin") origin:String, @Query("destination") destination:String, @Query("key") key:String): Call<GoogleDirectionsDTO>
    @GET("3/movie/popular?")
    fun getListPopularMovies(@Query("api_key")api_key:String, @Query("language")language:String):Call<MovieDTO>
    @GET("3/movie/{id}?")
    fun getMovieById(@Path("id")id:Int,@Query("api_key")api_key: String, @Query("language")language:String):Call<MovieDetailDTO>
}
