package com.example.ta.Model

class MKeranjang {
    var idP:String
    var judul:String
    var harga:Int
    var qty:Int
    var foto:String
    var foto_type:String
    var catatan:String

    constructor(
        idP:String,
        judul:String,
        harga:Int,
        qty:Int,
        foto:String,
        foto_type:String,
        catatan:String
    ){
        this.idP = idP
        this.judul = judul
        this.harga = harga
        this.qty  = qty
        this.foto = foto
        this.foto_type = foto_type
        this.catatan = catatan
    }

}