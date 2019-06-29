package com.example.ta.Model.cost

/**
 * Created by Robby Dianputra on 2/14/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Cost {

    @SerializedName("service")
    @Expose
    var service: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("cost")
    @Expose
    var cost: List<Cost_>? = null

}
