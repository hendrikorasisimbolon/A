package com.example.ta.Model

class MRiwayat {
    var id_Trans:String
    var user_id:String
    var created:String
    var ongkir:String
    var kurir:String
    var service:String
    var status:Int
    var resi:String
    var total:Int
    constructor(
        id_Trans:String,
        user_id:String,
        created:String,
        ongkir:String,
        kurir:String,
        service:String,
        status:Int,
        resi:String,
        total:Int
    ){
        this.id_Trans = id_Trans
        this.user_id = user_id
        this.created = created
        this.ongkir = ongkir
        this.kurir = kurir
        this.service = service
        this.status = status
        this.resi = resi
        this.total = total
    }
}