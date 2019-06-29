package com.example.ta.Model.cost

/**
 * Created by Robby Dianputra on 2/14/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Status {

    @SerializedName("code")
    @Expose
    var code: Int? = null
    @SerializedName("description")
    @Expose
    var description: String? = null

}
