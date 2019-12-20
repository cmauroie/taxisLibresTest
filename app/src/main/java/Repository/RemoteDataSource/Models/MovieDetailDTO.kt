package Repository.RemoteDataSource.Models

import com.google.gson.annotations.SerializedName

class MovieDetailDTO (
    @SerializedName("overview") var overview:String,
    @SerializedName("original_title") var original_title:String,
    @SerializedName("poster_path") var poster_path:String,
    @SerializedName("backdrop_path") var backdrop_path:String,
    @SerializedName("release_date") var release_date: String,
    @SerializedName("revenue") var revenue: Int,
    @SerializedName("tagline") var tagline: String,
    @SerializedName("title") var title: String,
    @SerializedName("vote_average") var vote_average: Float
)