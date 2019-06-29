package com.example.ta.Model.kota

/**
 * Created by Robby Dianputra on 2/13/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Result(
    @field:SerializedName("city_id")
    @field:Expose
    var cityId: String?, @field:SerializedName("province_id")
    @field:Expose
    var provinceId: String?, @field:SerializedName("province")
    @field:Expose
    var province: String?, @field:SerializedName("type")
    @field:Expose
    var type: String?, @field:SerializedName("city_name")
    @field:Expose
    var cityName: String?, @field:SerializedName("postal_code")
    @field:Expose
    var postalCode: String?
) {

}
