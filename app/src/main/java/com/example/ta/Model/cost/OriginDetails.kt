package com.example.ta.Model.cost

/**
 * Created by Robby Dianputra on 2/14/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class OriginDetails {

    @SerializedName("city_id")
    @Expose
    var cityId: String? = null
    @SerializedName("province_id")
    @Expose
    var provinceId: String? = null
    @SerializedName("province")
    @Expose
    var province: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("city_name")
    @Expose
    var cityName: String? = null
    @SerializedName("postal_code")
    @Expose
    var postalCode: String? = null

}
