package com.example.ta.Model

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class MItemDetail {
    var id:Int
    var judul_produk:String
    var harga_normal:Double
    var harga_diskon:Double
    var diskon:Double
    var deskripsi:String
    var berat:Double
    var stok:Double
    var kat_id:Double
    var judul_kat:String
    var subkat_id:Double
    var judul_subkat:String
    var foto:String
    var foto_type:String

    constructor(
        id:Int,
        judul_produk:String,
        harga_normal:Double,
        harga_diskon:Double,
        diskon:Double,
        deksripsi:String,
        berat:Double,
        stok:Double,
        kat_id:Double,
        judul_kat:String,
        subkat_id:Double,
        judul_subkat:String,
        foto:String,
        foto_type:String
    ){
        this.id = id
        this.judul_produk = judul_produk
        this.harga_normal = harga_normal
        this.deskripsi = deksripsi
        this.berat = berat
        this.foto = foto
        this.foto_type = foto_type
        this.harga_diskon = harga_diskon
        this.stok = stok
        this.diskon = diskon
        this.subkat_id = subkat_id
        this.kat_id = kat_id
        this.judul_kat = judul_kat
        this.judul_subkat = judul_subkat
    }

    companion object{
        var data:ArrayList<MItemDetail> = ArrayList()

        fun reffilData(a:ArrayList<MItemDetail>): ArrayList<MItemDetail> {
            data.clear()
            data = ArrayList()
            data = a
            return data
        }

        fun getProducts(context: Context): ArrayList<MItemDetail> {
            var da:ArrayList<MItemDetail> = ArrayList()
            var url= Url_Volley.url_website +"/udemy/get_all_product.php"

            var rq: RequestQueue = Volley.newRequestQueue(context)

            var jar = JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->

                for(x in 0..response.length()-1)
                {
                    da.add(
                        MItemDetail(
                            response.getJSONObject(x).getInt("id_produk"),
                            response.getJSONObject(x).getString("judul_produk"),
                            response.getJSONObject(x).getDouble("harga_normal"),
                            response.getJSONObject(x).getDouble("harga_diskon"),
                            response.getJSONObject(x).getDouble("diskon"),
                            response.getJSONObject(x).getString("deskripsi"),
                            response.getJSONObject(x).getDouble("berat"),
                            response.getJSONObject(x).getDouble("stok"),
                            response.getJSONObject(x).getDouble("kat_id"),
                            response.getJSONObject(x).getString("judul_kategori"),
                            response.getJSONObject(x).getDouble("subkat_id"),
                            response.getJSONObject(x).getString("judul_subkategori"),
                            response.getJSONObject(x).getString("foto"),
                            response.getJSONObject(x).getString(("foto_type"))
                        )
                    )
                }

            }, Response.ErrorListener { error ->


            })
            rq.add(jar)

            return da
        }
    }

}