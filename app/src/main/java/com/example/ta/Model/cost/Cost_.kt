package com.example.ta.Model.cost

/**
 * Created by Robby Dianputra on 2/14/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Cost_ {

    @SerializedName("value")
    @Expose
    var value: Int? = null
    @SerializedName("etd")
    @Expose
    var etd: String? = null
    @SerializedName("note")
    @Expose
    var note: String? = null

}
