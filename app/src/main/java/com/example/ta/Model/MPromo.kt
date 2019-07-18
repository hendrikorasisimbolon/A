package com.example.ta.Model

class MPromo{
    var id_promo:Int
    var kode_promo:String
    var ket_promo:String
    var discount:Int
    var max_pembelian:Int

    constructor(
        id_promo:Int,
        kode_promo:String,
        ket_promo:String,
        discount:Int,
        max_pembelian:Int
    ){
        this.id_promo= id_promo
        this.kode_promo = kode_promo
        this.ket_promo = ket_promo
        this.discount = discount
        this.max_pembelian = max_pembelian
    }

}
