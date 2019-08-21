package com.example.ta

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.ta.Model.MCart
import com.example.ta.Model.Url_Volley
import com.example.ta.RiwayatDetailAact.Companion.id_trans
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.activity_testi.*
import kotlinx.android.synthetic.main.item_testi.view.*

class TestiAct : AppCompatActivity() {

    data class DataShowTesti(
        var id_produk:String,
        var judulproduk:String,
        var foto:String,
        var foto_type:String
    )

    var id_tras = ""

    var list:ArrayList<DataShowTesti> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testi)
        initToolbar()
//        id_trans = intent.getStringExtra("id_trans").toString()
        var url = Url_Volley.url_website + "/udemy/testi_show.php?id_trans="+ id_trans
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->
            for(x in 0..response.length()-1)
            {
                list.add(
                    DataShowTesti(
                        response.getJSONObject(x).getString("id_produk"),
                        response.getJSONObject(x).getString("judul_produk"),
                        response.getJSONObject(x).getString("foto"),
                        response.getJSONObject(x).getString("foto_type")
                    )
                )
            }
            var adp = TestiShowAdapter(this,list)
            rv_testi.layoutManager= LinearLayoutManager(this)
            rv_testi.adapter=adp
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message,Toast.LENGTH_SHORT).show()
        })
        rq.add(jar)

    }
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Checkout"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_10)
        Tools.setSystemBarLight(this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.grey_60))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.action_done) {
            for(i in 0..list.count()-1)
            {
                var v:View = rv_testi.getChildAt(i)
                var e:EditText = v.findViewById(R.id.dt_testi)
                var r:RatingBar = v.findViewById(R.id.rt_br)

                Log.e("rrrrrrr", r.rating.toString())

                var url = Url_Volley.url_website+"/udemy/insert_testi.php?item_id="+list[i].id_produk+"&user_id="+MCart.user_id+"&rating="+r.rating.toInt().toString()+"&testi="+e.text.toString()
                var rq: RequestQueue = Volley.newRequestQueue(this)
                var jar= StringRequest(Request.Method.GET,url,Response.Listener { response ->
                    Log.e("Responsedari",response)
                }, Response.ErrorListener { error ->
                    Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
                })
                rq.add(jar)
            }
            selesai(id_trans)
            var intent = Intent(this, RiwayatAct::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    fun selesai(id_trans:String)
    {
        var url = Url_Volley.url_website+"/udemy/update_status.php?id_trans="+id_trans+"&status=5"
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar= StringRequest(Request.Method.GET,url,Response.Listener { response ->
            Log.e("Responsedariselesai",response)
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)
    }


    class TestiShowAdapter(var context: Context, var daftar:ArrayList<DataShowTesti>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val layout = LayoutInflater.from(context).inflate(R.layout.item_testi, parent, false)
            return TestiShowHolder(layout)
        }

        override fun getItemCount(): Int = daftar.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as TestiShowHolder).bind_item(
                daftar[position].judulproduk,daftar[position].foto,daftar[position].foto_type
            )
        }
        class TestiShowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind_item(jdl:String, ft:String, ftt:String)
            {
                itemView.txt_nmbarang.text = jdl
                Glide.with(itemView.context).load(Url_Volley.url_website+"/ecommerce/assets/images/produk/" + ft + ftt)
                    .into(itemView.ft_prod)
            }
        }
    }
}


