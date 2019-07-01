package com.example.ta.Model

class MService {
    var kode:String
    var service:String
    var tarif:Int
    var estimasi:String
    constructor(
        kode:String,
        service:String,
        tarif:Int,
        estimasi:String
    )
    {
        this.kode = kode
        this.service = service
        this.tarif = tarif
        this.estimasi = estimasi
    }
}