package Repository.RemoteDataSource.Models

import com.google.gson.annotations.SerializedName

data class MovieDTO(
    @SerializedName("page") var page: Integer,
    @SerializedName("total_results") var total_results: Integer,
    @SerializedName("total_pages") var total_pages: Integer,
    @SerializedName("results") var results: List<Results>
)

data class Results(
    @SerializedName("poster_path") var poster_path: String,
    @SerializedName("id") var id: Integer,
    @SerializedName("title") var title: String,
    @SerializedName("vote_average") var vote_average: Float,
    @SerializedName("release_date") var release_date: String
)
