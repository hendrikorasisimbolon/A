package com.example.ta.Model.cost

/**
 * Created by Robby Dianputra on 2/14/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Result(
    @field:SerializedName("code")
    @field:Expose
    var code: String?, @field:SerializedName("name")
    @field:Expose
    var name: String?, costs: List<Cost>
) {
    @SerializedName("costs")
    @Expose
    var costs: List<Cost>? = null

    init {
        this.costs = costs
    }

}
