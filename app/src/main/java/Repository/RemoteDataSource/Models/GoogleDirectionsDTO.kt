package Repository.RemoteDataSource.Models

import com.google.gson.annotations.SerializedName

class GoogleDirectionsDTO(
    @SerializedName("routes") var routes: List<Routes>
)

class Routes(
    @SerializedName("legs") var legs: List<Legs>
)

class Legs(
    @SerializedName("distance") var distance: Distance,
    @SerializedName("duration") var duration: Duration
)

class Distance(
    @SerializedName("text") var text: String,
    @SerializedName("value") var value: Float
)

class Duration(
    @SerializedName("text") var text: String,
    @SerializedName("value") var value: Float
)