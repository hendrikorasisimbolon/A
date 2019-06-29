package com.example.ta.Model


public class MBlog {
    var id_blog:Int
    var judul_blog:String
    var isi_blog:String
    var created:String
    var modified_by:String
    var foto:String
    var foto_type:String

    constructor(id_blog:Int,judul_blog:String,isi_blog:String,created:String,modified_by:String,foto:String,foto_type:String){
        this.id_blog = id_blog
        this.judul_blog = judul_blog
        this.isi_blog = isi_blog
        this.created = created
        this.modified_by = modified_by
        this.foto = foto
        this.foto_type = foto_type
    }

}