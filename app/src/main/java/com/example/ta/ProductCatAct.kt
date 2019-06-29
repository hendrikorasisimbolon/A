package com.example.ta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.Url_Volley.Companion.url_website
import kotlinx.android.synthetic.main.activity_product_cat.*

class ProductCatAct : AppCompatActivity() {

    companion object{
        var cat:String = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_cat)
        var url =url_website+"/udemy/get_category.php"
        //var url ="http://192.168.43.180/udemy//get_all_product.php"
        var rq:RequestQueue = Volley.newRequestQueue(this)
        var list = ArrayList<String>()
        var list_id = ArrayList<String>()

        var jar= JsonArrayRequest(Request.Method.GET,url,null,Response.Listener { response ->

            for (x in 0 .. response.length()-1)
            {
                list.add(response.getJSONObject(x).getString("judul_subkategori"))
                list_id.add(response.getJSONObject(x).getString("id_subkategori"))
            }

            var  adp = ArrayAdapter(this,R.layout.my_textview,list)

            list_cat.adapter=adp

        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
        })
        rq.add(jar)

        list_cat.setOnItemClickListener { parent, view, position, id ->

            //var cat:String = list[position]ssSSS
            cat= list_id[position]
            val obj = Intent(this,ItemsAct::class.java)
            obj.putExtra("cat",cat)
            startActivity(obj)

        }
    }
}
