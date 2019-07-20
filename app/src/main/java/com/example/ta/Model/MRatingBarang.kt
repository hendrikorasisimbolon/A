package com.example.ta.Model

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class MRatingBarang {
    var id:Int
    var judul_produk:String
    var harga_normal:Double
    var harga_diskon:Double
    var diskon:Double
    var deksripsi:String
    var berat:Double
    var stok:Double
    var kat_id:Double
    var judul_kat:String
    var subkat_id:Double
    var judul_subkat:String
    var foto:String
    var foto_type:String
    var rating: Double
    var algo:String
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
        foto_type:String,
        rating:Double,
        algo:String
    ) {
        this.id = id
        this.judul_produk = judul_produk
        this.harga_normal = harga_normal
        this.deksripsi = deksripsi
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
        this.rating = rating
        this.algo = algo
    }
    companion object{
        var list:ArrayList<MRatingBarang> = ArrayList()
        var daftarRating:ArrayList<MAlgo> = ArrayList()

        fun getAllRating(context: Context): ArrayList<MRatingBarang> {
            var li:ArrayList<MRatingBarang> = ArrayList()
            var url= Url_Volley.url_website +"/udemy/get_all_rating.php"

            var rq: RequestQueue = Volley.newRequestQueue(context)
            var jar = JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->

                for(x in 0..response.length()-1)
                {
                    list.add(
                        MRatingBarang(
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
                            response.getJSONObject(x).getString("foto_type"),
                            response.getJSONObject(x).getDouble("rating"),
                                ""
                        )
                    )

//

                }

            }, Response.ErrorListener { error ->
                Toast.makeText(context,error.message, Toast.LENGTH_LONG).show()

            })
            rq.add(jar)
            return li
        }

        fun getAlgo(context: Context): ArrayList<MAlgo> {
            var li:ArrayList<MAlgo> = ArrayList()
            var url1= Url_Volley.url_website +":3000/?name="+ MCart.user_id
            var rq1: RequestQueue = Volley.newRequestQueue(context)
            var jar1 = JsonArrayRequest(Request.Method.GET,url1,null, Response.Listener { response ->
                for (x in 0..response.length()-1)
                {
//                    Log.e("Algoritma", response.getJSONObject(x).getDouble("ratingp").toString())
                    li.add(
                       MAlgo(response.getJSONObject(x).getString("user"),
                           response.getJSONObject(x).getDouble("ratingp"),
                           response.getJSONObject(x).getInt("item"))
                    )
                }

//                for(x in 0..response.length()-1)
//                {
//                    for(i in 0..list.size-1)
//                    {
//                        if(response.getJSONObject(x).getInt(("item")) == list[i].id){
//                            list[i].rating = response.getJSONObject(x).getDouble(("ratingp"))
//                            li.add(
//                                MRatingBarang(
//                                    list[i].id, list[i].judul_produk,list[i].harga_normal,list[i].deksripsi, list[i].berat, list[i].foto, list[i].foto_type, list[i].harga_diskon,
//                                    list[i].stok, list[i].diskon, list[i].subkat_id, list[i].kat_id, response.getJSONObject(x).getDouble(("ratingp"))
//                                )
//                            )
//                            Log.e("cocok", list[i].rating.toString())
//                        }
//
//                    }
//                }
            }, Response.ErrorListener { error ->
                Toast.makeText(context,error.message, Toast.LENGTH_LONG).show()

            })
            rq1.add(jar1)
            return li
        }
    }
}