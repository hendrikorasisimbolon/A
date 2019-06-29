package com.example.ta.Model

public class MItems {
    var id:Int
    var judul_produk:String
    var harga_normal:Double
    var deksripsi:String
    var foto:String
    var foto_type:String

    constructor(id:Int,judul_produk:String,harga_normal:Double,deksripsi:String,foto:String,foto_type:String){
        this.id = id
        this.judul_produk = judul_produk
        this.harga_normal = harga_normal
        this.deksripsi = deksripsi
        this.foto = foto
        this.foto_type = foto_type
    }

}