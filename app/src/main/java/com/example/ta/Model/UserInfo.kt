package com.example.ta.Model

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
    val username:String,
    val name:String,
    val password:String,
    val email:String,
    val phone:String,
    val nama_provinsi:String,
    val nama_kota:String,
    val address:String,
    val umur:String,
    val lahir: String,
    val created_on:Int)