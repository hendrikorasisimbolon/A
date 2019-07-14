package com.example.ta.Model

import android.provider.ContactsContract
import java.util.*

//class UserInfo {
//    var name:String
//    var email:String
//    var phone:String
//    var provinsi:Int
//    var kota:Int
//    var address:String
//    var created_on:Int
//
//    constructor(name:String,email:String,phone:String,provinsi:Int,kota:Int,address:String,created_on:Int)
//    {
//        this.name = name
//        this.email = email
//        this.phone = phone
//        this.provinsi = provinsi
//        this.kota = kota
//        this.address = address
//        this.created_on = created_on
//    }
//}

data class UserInfo(
    val id: Int,
    val name:String,
    val username:String,
    val password:String,
    val email:String,
    val phone:String,
    val nama_provinsi:String,
    val id_provinsi:String,
    val nama_kota:String,
    val id_kota:String,
    val address:String,
    val umur:String,
    val lahir: String,
    val photo: String,
    val photo_type:String,
    val created_on:Int)