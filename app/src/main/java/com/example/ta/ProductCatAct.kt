//package com.example.ta
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.android.volley.Request
//import com.android.volley.RequestQueue
//import com.android.volley.Response
//import com.android.volley.toolbox.JsonArrayRequest
//import com.android.volley.toolbox.Volley
//import com.example.ta.Adapter.KategoriAdapter
//import com.example.ta.Model.MKategori
//import com.example.ta.Model.Url_Volley.Companion.url_website
//import kotlinx.android.synthetic.main.activity_product_cat.*
//
//class ProductCatAct : AppCompatActivity() {
//
//    companion object{
//        var cat:String = "0"
//    }
//    var kategori:ArrayList<MKategori> = ArrayList()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_product_cat)
//        var url =url_website+"/udemy/get_category.php"
//        //var url ="http://192.168.43.180/udemy//get_all_product.php"
//        var rq:RequestQueue = Volley.newRequestQueue(this)
//
//
//        var jar= JsonArrayRequest(Request.Method.GET,url,null,Response.Listener { response ->
//
//            for (x in 0 .. response.length()-1)
//            {
//                kategori.add(MKategori(
//                    response.getJSONObject(x).getString("id_subkategori"),
//                    response.getJSONObject(x).getString("id_kat"),
//                    response.getJSONObject(x).getString("judul_subkategori")
//                ))
//            }
//
//            var  adp = KategoriAdapter(this,R.layout.category,kategori)
//
//            list_cat.adapter=adp
//
//        }, Response.ErrorListener { error ->
//            Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
//        })
//        rq.add(jar)
//
//        list_cat.setOnItemClickListener { parent, view, position, id ->
//
//            //var cat:String = list[position]ssSSS
//            cat= kategori[position].id_subkat
//            val obj = Intent(this,ItemsAct::class.java)
//            obj.putExtra("cat",cat)
//            startActivity(obj)
//
//        }
//    }
//}
