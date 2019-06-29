package com.example.ta.Model.cost

/**
 * Created by Robby Dianputra on 2/14/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Rajaongkir {

    @SerializedName("query")
    @Expose
    var query: Query? = null
    @SerializedName("status")
    @Expose
    var status: Status? = null
    @SerializedName("origin_details")
    @Expose
    var originDetails: OriginDetails? = null
    @SerializedName("destination_details")
    @Expose
    var destinationDetails: DestinationDetails? = null
    @SerializedName("results")
    @Expose
    var results: List<Result>? = null

}
